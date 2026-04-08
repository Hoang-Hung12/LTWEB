const API = 'http://localhost:8080';

  document.addEventListener('DOMContentLoaded', async () => {
    await Promise.all([loadDons(), loadKH(), loadSans()]);
  });

  async function loadDons() {
    try {
      const res  = await fetch(API + '/api/dondatsan');
      const dons = await res.json();
      dons.sort((a,b) => new Date(b.ngayDat||0) - new Date(a.ngayDat||0));

      const choTT = dons.filter(d => d.trangThai === 'Chờ xác nhận thanh toán').length;
      const cho  = dons.filter(d => d.trangThai === 'Chờ duyệt').length;
      const xn   = dons.filter(d => d.trangThai === 'Đã xác nhận').length;
      const ht   = dons.filter(d => d.trangThai === 'Đã hoàn thành').length;
      const huy  = dons.filter(d => d.trangThai === 'Đã hủy').length;

      document.getElementById('kpi-total').textContent   = dons.length;
      document.getElementById('kpi-pending').textContent = choTT;

      // Doanh thu
      const donHT   = dons.filter(d => d.trangThai === 'Đã hoàn thành');
      const revTotal = donHT.reduce((s,d) => s + (d.tienSan||0), 0);
      const revCoc   = donHT.reduce((s,d) => s + (d.tienCoc||0), 0);
      const revDiem  = donHT.reduce((s,d) => s + (d.diemThuong||0), 0);
      document.getElementById('rev-total').textContent = fmtMoney(revTotal);
      document.getElementById('rev-coc').textContent   = fmtMoney(revCoc);
      document.getElementById('rev-diem').textContent  = revDiem.toLocaleString('vi-VN') + ' điểm';

      // Donut chart
      new Chart(document.getElementById('donutChart'), {
        type: 'doughnut',
        data: {
          labels: ['Chờ xác nhận TT','Chờ duyệt','Đã xác nhận','Hoàn thành','Đã hủy'],
          datasets: [{ data:[choTT,cho,xn,ht,huy], backgroundColor:['#fb7185','#fbbf24','#22c55e','#3b82f6','#ef4444'], borderWidth:2 }]
        },
        options: { plugins:{ legend:{ position:'bottom',labels:{font:{size:12}} } }, cutout:'65%' }
      });

      // Bảng gần đây
      const recent = dons.slice(0, 8);
      const tbody  = document.getElementById('recent-tbody');
      tbody.innerHTML = recent.map(d => {
        const ttClass = { 'Chờ duyệt':'badge-soft-warning','Đã xác nhận':'badge-soft-success','Đã hoàn thành':'badge-soft-primary','Đã hủy':'badge-soft-danger' }[d.trangThai] || 'badge-soft-secondary';
        const ngay = d.ngayDa ? fmtDate(d.ngayDa) : '—';
        return `<tr>
          <td class="ps-4"><strong class="text-primary">${d.maDon}</strong></td>
          <td>${d.tenKhachHang||'—'}</td>
          <td>${d.tenSan||'—'}</td>
          <td>${ngay}</td>
          <td class="text-danger fw-bold">${fmtMoney(d.tienSan)}</td>
          <td><span class="badge-soft ${ttClass}">${d.trangThai||'—'}</span></td>
        </tr>`;
      }).join('') || '<tr><td colspan="6" class="text-center text-muted py-3">Chưa có đơn nào</td></tr>';
    } catch(e) {
      console.error(e);
    }
  }

  async function loadKH() {
    try {
      const res = await fetch(API + '/api/taikhoan');
      const all = await res.json();
      const khList = all.filter(u => (u.vaiTro||'').toUpperCase() !== 'ADMIN');
      document.getElementById('kpi-kh').textContent = khList.length;
    } catch {}
  }

  async function loadSans() {
    try {
      const res  = await fetch(API + '/api/san');
      const sans = await res.json();
      const active = sans.filter(s => (s.trangThai||'').toUpperCase() !== 'MAINTENANCE').length;
      const maint  = sans.length - active;
      document.getElementById('kpi-san').textContent          = active;
      document.getElementById('kpi-maintenance').textContent  = maint;
    } catch {}
  }

  function fmtDate(d) {
    const p = String(d).split('-');
    return p.length === 3 ? `${p[2]}/${p[1]}/${p[0]}` : d;
  }
  function fmtMoney(n) {
    if (n == null) return '—';
    return Number(n).toLocaleString('vi-VN') + 'đ';
  }
