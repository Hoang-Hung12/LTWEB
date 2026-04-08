
  let allDons = [];

  document.addEventListener('DOMContentLoaded', async () => {
    const user = getCurrentUser();
    if (!user) { window.location.href = 'login.html'; return; }
    const av = document.getElementById('nav-user-avatar');
    if (av) av.textContent = (user.hoTen || 'U')[0].toUpperCase();

    try {
      const res = await fetch(API_BASE + '/api/dondatsan/lich-su/' + user.maTK);
      if (!res.ok) throw new Error();
      allDons = await res.json();
      allDons.sort((a,b) => new Date(b.ngayDat||0) - new Date(a.ngayDat||0));
      updateStats();
      renderList();
      document.getElementById('stats-bar').style.display = 'flex';
      document.getElementById('filter-bar').style.display = 'flex';
    } catch {
      document.getElementById('list-wrap').innerHTML = '<p class="text-danger text-center py-4">KhÃ´ng táº£i Ä‘Æ°á»£c dá»¯ liá»‡u!</p>';
    }
  });

  function updateStats() {
    document.getElementById('st-total').textContent   = allDons.length;
    document.getElementById('st-done').textContent    = allDons.filter(d => d.trangThai === 'ÄÃ£ hoÃ n thÃ nh').length;
    document.getElementById('st-pending').textContent = allDons.filter(d =>
      d.trangThai === 'Chá» xÃ¡c nháº­n thanh toÃ¡n' || d.trangThai === 'Chá» duyá»‡t').length;
    document.getElementById('st-cancel').textContent  = allDons.filter(d => d.trangThai === 'ÄÃ£ há»§y').length;
    const pts = allDons.reduce((s,d) => s + (d.diemThuong||0), 0);
    document.getElementById('st-points').textContent  = pts;
  }

  function renderList() {
    const filter = document.getElementById('filter-tt-lich').value;
    const data   = filter ? allDons.filter(d => d.trangThai === filter) : allDons;
    const wrap   = document.getElementById('list-wrap');
    if (data.length === 0) {
      wrap.innerHTML = `<div class="empty-state"><i class="fas fa-calendar-xmark"></i><h5>KhÃ´ng cÃ³ Ä‘Æ¡n nÃ o</h5><p>HÃ£y Ä‘áº·t sÃ¢n Ä‘á»ƒ báº¯t Ä‘áº§u!</p><a href="danh-sach-san.html" class="btn btn-primary">Äáº·t sÃ¢n ngay</a></div>`;
      return;
    }
    wrap.innerHTML = data.map(don => {
      const iconClass = statusClass(don.trangThai);
      const icon      = statusIcon(don.trangThai);
      const ngay = don.ngayDa ? formatDate2(don.ngayDa) : 'â€”';
      const gio  = [(don.gioBatDau||'').substring(0,5), (don.gioKetThuc||'').substring(0,5)].filter(Boolean).join(' - ');
      const canCancel = don.trangThai === 'Chá» xÃ¡c nháº­n thanh toÃ¡n' || don.trangThai === 'Chá» duyá»‡t';
      return `
      <div class="history-card">
        <div class="history-icon ${iconClass}">${icon}</div>
        <div class="flex-grow-1">
          <div class="d-flex justify-content-between align-items-start flex-wrap gap-2">
            <div>
              <h6 class="fw-bold mb-1">${don.tenSan || 'â€”'}</h6>
              <p class="text-muted mb-1" style="font-size:14px"><i class="fas fa-calendar me-1"></i>${ngay} &nbsp;|&nbsp; <i class="fas fa-clock me-1"></i>${gio}</p>
              <p class="text-muted mb-0" style="font-size:13px">MÃ£ Ä‘Æ¡n: <code>${don.maDon}</code></p>
            </div>
            <div class="text-end">
              <div class="badge-tt ${iconClass} mb-1">${don.trangThai || 'â€”'}</div>
              <div class="fw-bold text-primary">${formatCurrency(don.tienSan)}</div>
              ${don.diemThuong ? `<div class="text-success" style="font-size:12px">+${don.diemThuong} Ä‘iá»ƒm</div>` : ''}
            </div>
          </div>
          ${canCancel ? `<button class="btn btn-outline-danger btn-sm mt-2" onclick="huyDon('${don.maDon}')"><i class="fas fa-times me-1"></i>Há»§y Ä‘Æ¡n</button>` : ''}
        </div>
      </div>`;
    }).join('');
  }

  async function huyDon(maDon) {
    if (!confirm('XÃ¡c nháº­n há»§y Ä‘Æ¡n ' + maDon + '?')) return;
    try {
      const res = await fetch(API_BASE + '/api/dondatsan/' + maDon + '/trang-thai?trangThai=ÄÃ£ há»§y', { method: 'PATCH' });
      if (!res.ok) throw new Error();
      const idx = allDons.findIndex(d => d.maDon === maDon);
      if (idx >= 0) allDons[idx].trangThai = 'ÄÃ£ há»§y';
      updateStats();
      renderList();
    } catch {
      alert('KhÃ´ng há»§y Ä‘Æ°á»£c Ä‘Æ¡n!');
    }
  }

  function statusClass(tt) {
    if (!tt) return 'pending';
    const t = tt.toLowerCase();
    if (t.includes('xÃ¡c nháº­n') && !t.includes('chá»')) return 'confirmed';
    if (t.includes('hoÃ n thÃ nh')) return 'done';
    if (t.includes('há»§y')) return 'cancelled';
    return 'pending';
  }
  function statusIcon(tt) {
    const c = statusClass(tt);
    const map = {confirmed:'<i class="fas fa-check"></i>',done:'<i class="fas fa-trophy"></i>',cancelled:'<i class="fas fa-times"></i>',pending:'<i class="fas fa-clock"></i>'};
    return map[c] || map.pending;
  }
  function formatDate2(d) {
    if (!d) return 'â€”';
    const p = String(d).split('-');
    return p.length === 3 ? p[2]+'/'+p[1]+'/'+p[0] : d;
  }

