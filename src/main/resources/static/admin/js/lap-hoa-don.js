const API   = 'http://localhost:8080';
  let allDons = [];
  let selectedDon = null;

  document.addEventListener('DOMContentLoaded', loadDons);

  async function loadDons() {
    try {
      const res = await fetch(API + '/api/dondatsan');
      allDons = await res.json();
      allDons.sort((a,b) => new Date(b.ngayDat||0) - new Date(a.ngayDat||0));
      filterDons();
    } catch {
      document.getElementById('don-list').innerHTML =
        '<p class="text-danger text-center py-3">Lá»—i káº¿t ná»‘i!</p>';
    }
  }

  function filterDons() {
    const q  = (document.getElementById('search-don').value||'').toLowerCase();
    const tt = document.getElementById('filter-tt-hd').value;
    const data = allDons.filter(d => {
      const mTT = !tt || d.trangThai === tt;
      const mQ  = !q  || (d.maDon||'').toLowerCase().includes(q)
                       || (d.tenKhachHang||'').toLowerCase().includes(q);
      return mTT && mQ;
    });
    const list = document.getElementById('don-list');
    const clsMap = {
      'ÄÃ£ xÃ¡c nháº­n':'badge-soft-success','ÄÃ£ hoÃ n thÃ nh':'badge-soft-primary',
      'Chá» duyá»‡t':'badge-soft-warning','ÄÃ£ há»§y':'badge-soft-danger'
    };
    list.innerHTML = data.length === 0
      ? '<p class="text-muted text-center py-3 small">KhÃ´ng cÃ³ Ä‘Æ¡n phÃ¹ há»£p.</p>'
      : data.map(d => {
          const ngay = d.ngayDa ? fmtDate(d.ngayDa) : 'â€”';
          const gio  = [(d.gioBatDau||'').substring(0,5),(d.gioKetThuc||'').substring(0,5)].join(' - ');
          const cls  = clsMap[d.trangThai] || 'badge-soft-secondary';
          const active = selectedDon && selectedDon.maDon===d.maDon ? 'selected' : '';
          return `<div class="don-item p-3 border-bottom ${active}" onclick="selectDon('${d.maDon}')">
            <div class="d-flex justify-content-between align-items-start">
              <strong class="text-primary small font-monospace">${d.maDon}</strong>
              <span class="badge-soft ${cls}" style="font-size:10px;padding:2px 7px;border-radius:10px">${d.trangThai||'â€”'}</span>
            </div>
            <div class="fw-bold small mt-1">${d.tenKhachHang||'â€”'}</div>
            <div class="small text-muted">${d.tenSan||'â€”'}</div>
            <div class="small text-muted">${ngay} | ${gio}</div>
            <div class="small fw-bold text-danger mt-1">${fmtMoney(d.tienSan)}</div>
          </div>`;
        }).join('');
  }

  function selectDon(maDon) {
    selectedDon = allDons.find(d => d.maDon === maDon);
    if (!selectedDon) return;
    filterDons();
    renderInvoice(selectedDon);
  }

  function renderInvoice(d) {
    document.getElementById('empty-state').style.display = 'none';
    document.getElementById('inv-content').style.display = 'block';
    document.getElementById('hd-success').classList.add('d-none');
    document.getElementById('hd-error').classList.add('d-none');

    const ngay  = d.ngayDa ? fmtDate(d.ngayDa) : 'â€”';
    const gio   = [(d.gioBatDau||'').substring(0,5),(d.gioKetThuc||'').substring(0,5)].join(' â€“ ');
    const tienCon = Math.max(0, (d.tienSan||0) - (d.tienCoc||0) - ((d.diemSuDung||0)*100));
    const clsMap  = {'ÄÃ£ xÃ¡c nháº­n':'badge-soft-success','ÄÃ£ hoÃ n thÃ nh':'badge-soft-primary','Chá» duyá»‡t':'badge-soft-warning'};

    document.getElementById('inv-ma').textContent        = '(ChÆ°a xuáº¥t)';
    document.getElementById('inv-ngaylap').textContent   = 'NgÃ y láº­p: ' + new Date().toLocaleDateString('vi-VN');
    document.getElementById('inv-kh').textContent        = d.tenKhachHang || 'â€”';
    document.getElementById('inv-sdt').textContent       = d.sdtKhachHang || d.maKH || 'â€”';
    document.getElementById('inv-madon').textContent     = d.maDon;
    document.getElementById('inv-ngayda').textContent    = 'NgÃ y Ä‘áº·t: ' + (d.ngayDat ? new Date(d.ngayDat).toLocaleDateString('vi-VN') : 'â€”');
    document.getElementById('inv-tiensan').textContent   = fmtMoney(d.tienSan);
    document.getElementById('inv-tiencocRow').textContent= '- ' + fmtMoney(d.tienCoc);
    document.getElementById('inv-total').textContent     = fmtMoney(tienCon);
    document.getElementById('inv-san').textContent       = d.tenSan || d.maSan || 'â€”';
    document.getElementById('inv-ngaydaInfo').textContent= ngay;
    document.getElementById('inv-gio').textContent       = gio;
    document.getElementById('inv-diemthuong').textContent= (d.diemThuong||0) + ' Ä‘iá»ƒm';
    document.getElementById('inv-status-badge').innerHTML= `<span class="badge-soft ${clsMap[d.trangThai]||'badge-soft-secondary'}">${d.trangThai||'â€”'}</span>`;

    if ((d.diemSuDung||0) > 0) {
      document.getElementById('inv-diem-row').style.display = '';
      document.getElementById('inv-diemRow').textContent = '- ' + fmtMoney(d.diemSuDung * 100);
    } else {
      document.getElementById('inv-diem-row').style.display = 'none';
    }

    const canExport = ['ÄÃ£ xÃ¡c nháº­n','ÄÃ£ hoÃ n thÃ nh'].includes(d.trangThai);
    document.getElementById('btn-xuat').disabled = !canExport;
  }

  async function xuatHoaDon() {
    if (!selectedDon) return;
    const admin   = JSON.parse(localStorage.getItem('arenax_user') || '{}');
    const maAdmin = admin.maTK || 'TK001';
    const btn     = document.getElementById('btn-xuat');
    const succ    = document.getElementById('hd-success');
    const errEl   = document.getElementById('hd-error');
    succ.classList.add('d-none');
    errEl.classList.add('d-none');
    btn.disabled  = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Äang xuáº¥t...';
    try {
      const res = await fetch(
        `${API}/api/hoadon/xuat?maDon=${encodeURIComponent(selectedDon.maDon)}&maQLLap=${encodeURIComponent(maAdmin)}`,
        { method: 'POST' }
      );
      if (!res.ok) {
        const body = await res.text();
        let msg = body;
        try { msg = JSON.parse(body).message || body; } catch {}
        throw new Error(msg);
      }
      const hd = await res.json();
      document.getElementById('inv-ma').textContent = hd.maHoaDon;
      document.getElementById('inv-ngaylap').textContent = 'NgÃ y láº­p: ' + new Date(hd.ngayLap).toLocaleDateString('vi-VN');
      succ.innerHTML = `âœ… Xuáº¥t hÃ³a Ä‘Æ¡n <strong>${hd.maHoaDon}</strong> thÃ nh cÃ´ng! CÃ²n láº¡i: <strong>${fmtMoney(hd.tongThanhToan)}</strong>`;
      succ.classList.remove('d-none');
      // cáº­p nháº­t local
      const idx = allDons.findIndex(d => d.maDon === selectedDon.maDon);
      if (idx >= 0) { allDons[idx].trangThai = 'ÄÃ£ hoÃ n thÃ nh'; selectedDon = allDons[idx]; }
      filterDons();
      btn.innerHTML = '<i class="fas fa-check me-1"></i>ÄÃ£ xuáº¥t';
    } catch(e) {
      errEl.textContent = 'âŒ ' + (e.message || 'Vui lÃ²ng thá»­ láº¡i!');
      errEl.classList.remove('d-none');
      btn.disabled  = false;
      btn.innerHTML = '<i class="fas fa-file-invoice-dollar me-1"></i>Xuáº¥t HÃ³a ÄÆ¡n';
    }
  }

  function clearInvoice() {
    selectedDon = null;
    document.getElementById('empty-state').style.display = 'block';
    document.getElementById('inv-content').style.display = 'none';
    filterDons();
  }

  function fmtDate(d) {
    const p = String(d).split('-');
    return p.length === 3 ? `${p[2]}/${p[1]}/${p[0]}` : d;
  }
  function fmtMoney(n) {
    if (n == null) return 'â€”';
    return Number(n).toLocaleString('vi-VN') + 'Ä‘';
  }
</script>
</body>

