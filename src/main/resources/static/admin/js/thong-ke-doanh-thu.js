const API = 'http://localhost:8080';
  let allDons  = [];
  let chartInst = null;

  document.addEventListener('DOMContentLoaded', async () => {
    // Set default range: last 30 days
    const today = new Date();
    const from  = new Date(today); from.setDate(from.getDate() - 29);
    document.getElementById('date-to').value   = today.toISOString().split('T')[0];
    document.getElementById('date-from').value = from.toISOString().split('T')[0];

    try {
      const res = await fetch(API + '/api/dondatsan');
      if (!res.ok) throw new Error('HTTP ' + res.status);
      allDons = await res.json();
      applyFilter();
    } catch (e) {
      console.error(e);
      showServerAlert('KhÃ´ng táº£i Ä‘Æ°á»£c dá»¯ liá»‡u thá»‘ng kÃª tá»« server. Vui lÃ²ng thá»­ láº¡i.');
      document.getElementById('detail-tbody').innerHTML =
        '<tr><td colspan="5" class="text-center text-danger py-4">Lá»—i káº¿t ná»‘i server! <button class="btn btn-sm btn-outline-primary ms-2" onclick="location.reload()">Táº£i láº¡i</button></td></tr>';
    }
  });

  function applyFilter() {
    const from = document.getElementById('date-from').value;
    const to   = document.getElementById('date-to').value;
    let filtered = allDons.filter(d => d.trangThai === 'ÄÃ£ hoÃ n thÃ nh');
    if (from) filtered = filtered.filter(d => (d.ngayDa||'') >= from);
    if (to)   filtered = filtered.filter(d => (d.ngayDa||'') <= to);
    renderStats(filtered);
    renderChart(filtered, from, to);
    renderDetailTable(filtered);
    renderTopSan(filtered);
  }

  function resetFilter() {
    const today = new Date();
    const from  = new Date(today); from.setDate(from.getDate() - 29);
    document.getElementById('date-to').value   = today.toISOString().split('T')[0];
    document.getElementById('date-from').value = from.toISOString().split('T')[0];
    applyFilter();
  }

  function renderStats(data) {
    const total = data.reduce((s,d) => s + (d.tienSan||0), 0);
    const avg   = data.length ? total / data.length : 0;
    const huy   = allDons.filter(d => d.trangThai === 'ÄÃ£ há»§y').length;
    document.getElementById('stat-rev').textContent  = fmtMoney(total);
    document.getElementById('stat-done').textContent = data.length;
    document.getElementById('stat-avg').textContent  = fmtMoney(Math.round(avg));
    document.getElementById('stat-huy').textContent  = huy;
  }

  function renderChart(data, from, to) {
    // Build date range
    const days = [];
    const fromDate = from ? new Date(from) : new Date(new Date().setDate(new Date().getDate()-29));
    const toDate   = to   ? new Date(to)   : new Date();
    for (let d = new Date(fromDate); d <= toDate; d.setDate(d.getDate()+1)) {
      days.push(d.toISOString().split('T')[0]);
    }
    const revenueByDay = {};
    days.forEach(d => revenueByDay[d] = 0);
    data.forEach(d => {
      const key = String(d.ngayDa).split('T')[0];
      if (revenueByDay.hasOwnProperty(key)) revenueByDay[key] += (d.tienSan||0);
    });
    const labels   = days.map(d => { const p=d.split('-'); return p[2]+'/'+p[1]; });
    const values   = days.map(d => revenueByDay[d]);

    if (chartInst) chartInst.destroy();
    chartInst = new Chart(document.getElementById('revenueChart'), {
      type: 'bar',
      data: {
        labels,
        datasets: [{
          label: 'Doanh thu (Ä‘)',
          data: values,
          backgroundColor: 'rgba(13,110,253,0.7)',
          borderColor: '#0d6efd',
          borderWidth: 1,
          borderRadius: 4
        }]
      },
      options: {
        plugins: { legend: { display: false } },
        scales: {
          y: { ticks: { callback: v => (v/1000000).toFixed(1) + 'tr' }, beginAtZero: true }
        }
      }
    });
  }

  function renderDetailTable(data) {
    // Group by day
    const byDay = {};
    data.forEach(d => {
      const key = String(d.ngayDa).split('T')[0];
      if (!byDay[key]) byDay[key] = { count:0, tienSan:0, tienCoc:0, diem:0 };
      byDay[key].count++;
      byDay[key].tienSan += (d.tienSan||0);
      byDay[key].tienCoc += (d.tienCoc||0);
      byDay[key].diem    += (d.diemThuong||0);
    });
    const rows = Object.entries(byDay).sort((a,b) => b[0].localeCompare(a[0]));
    const tbody = document.getElementById('detail-tbody');
    tbody.innerHTML = rows.length === 0
      ? '<tr><td colspan="5" class="text-center text-muted py-4">KhÃ´ng cÃ³ dá»¯ liá»‡u trong khoáº£ng thá»i gian nÃ y.</td></tr>'
      : rows.map(([d, v]) => {
          const p = d.split('-');
          return `<tr>
            <td class="ps-4 fw-bold">${p[2]}/${p[1]}/${p[0]}</td>
            <td>${v.count}</td>
            <td class="text-primary fw-bold">${fmtMoney(v.tienSan)}</td>
            <td class="text-success fw-bold">${fmtMoney(v.tienCoc)}</td>
            <td class="text-end pe-4">${v.diem.toLocaleString('vi-VN')} Ä‘iá»ƒm</td>
          </tr>`;
        }).join('');
  }

  function renderTopSan(data) {
    const bySan = {};
    data.forEach(d => {
      const key = d.tenSan || d.maSan || 'â€”';
      bySan[key] = (bySan[key]||0) + (d.tienSan||0);
    });
    const sorted = Object.entries(bySan).sort((a,b) => b[1]-a[1]).slice(0,5);
    document.getElementById('top-san-tbody').innerHTML = sorted.length === 0
      ? '<tr><td colspan="2" class="text-center text-muted py-2">ChÆ°a cÃ³</td></tr>'
      : sorted.map(([name,rev]) => `<tr>
          <td class="ps-3 small">${name}</td>
          <td class="text-end pe-3 fw-bold small text-primary">${fmtMoney(rev)}</td>
        </tr>`).join('');
  }

  function exportCSV() {
    const from = document.getElementById('date-from').value;
    const to   = document.getElementById('date-to').value;
    let filtered = allDons.filter(d => d.trangThai === 'ÄÃ£ hoÃ n thÃ nh');
    if (from) filtered = filtered.filter(d => (d.ngayDa||'') >= from);
    if (to)   filtered = filtered.filter(d => (d.ngayDa||'') <= to);

    const rows = [['MÃ£ Ä‘Æ¡n','KhÃ¡ch hÃ ng','SÃ¢n','NgÃ y Ä‘Ã¡','Giá»','Tiá»n sÃ¢n','Tiá»n cá»c','Äiá»ƒm thÆ°á»Ÿng']];
    filtered.forEach(d => {
      rows.push([
        d.maDon, d.tenKhachHang||'', d.tenSan||'', d.ngayDa||'',
        `${(d.gioBatDau||'').substring(0,5)}-${(d.gioKetThuc||'').substring(0,5)}`,
        d.tienSan||0, d.tienCoc||0, d.diemThuong||0
      ]);
    });
    const csv  = rows.map(r => r.join(',')).join('\n');
    const blob = new Blob(['\uFEFF'+csv], {type:'text/csv;charset=utf-8'});
    const url  = URL.createObjectURL(blob);
    const a    = document.createElement('a');
    a.href     = url;
    a.download = `doanh-thu-${from||'all'}-den-${to||'all'}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }

  function fmtMoney(n) {
    if (n == null) return 'â€”';
    return Number(n).toLocaleString('vi-VN') + 'Ä‘';
  }
  function showServerAlert(msg) {
    document.getElementById('server-alert').innerHTML = `
      <div class="alert alert-warning alert-dismissible fade show" role="alert">
        ${msg}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    `;
  }
</script>
</body>

