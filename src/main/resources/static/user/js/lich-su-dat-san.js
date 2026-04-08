let allDons = [];

document.addEventListener('DOMContentLoaded', async () => {
    const user = getCurrentUser();
    if (!user) { window.location.href = 'login.html'; return; }
    const av = document.getElementById('nav-user-avatar');
    if (av) av.textContent = (user.hoTen || 'U')[0].toUpperCase();

    try {
        const res = await fetch(API_BASE + '/api/dondatsan/lich-su/' + user.maTK);
        if (!res.ok) throw new Error('HTTP ' + res.status);
        allDons = await res.json();
        allDons.sort((a,b) => new Date(b.ngayDat||0) - new Date(a.ngayDat||0));
        updateStats();
        renderList();
        document.getElementById('stats-bar').style.display = 'flex';
        document.getElementById('filter-bar').style.display = 'flex';
    } catch(e) {
        document.getElementById('list-wrap').innerHTML =
            '<p class="text-danger text-center py-4"><i class="fas fa-exclamation-circle me-2"></i>Không tải được dữ liệu! Kiểm tra backend đã chạy chưa.</p>';
    }
});

// Đúng với trạng thái trong DB
function updateStats() {
    document.getElementById('st-total').textContent   = allDons.length;
    document.getElementById('st-done').textContent    = allDons.filter(d => d.trangThai === 'Đã hoàn thành').length;
    document.getElementById('st-pending').textContent = allDons.filter(d =>
        d.trangThai === 'Chờ duyệt' || d.trangThai === 'Đã xác nhận').length;
    document.getElementById('st-cancel').textContent  = allDons.filter(d => d.trangThai === 'Đã hủy').length;
    const pts = allDons.reduce((s,d) => s + (d.diemThuong||0), 0);
    document.getElementById('st-points').textContent  = pts;
}

function renderList() {
    const filter = document.getElementById('filter-tt-lich').value;
    const data   = filter ? allDons.filter(d => d.trangThai === filter) : allDons;
    const wrap   = document.getElementById('list-wrap');
    if (data.length === 0) {
        wrap.innerHTML = `<div class="empty-state">
          <i class="fas fa-calendar-xmark"></i>
          <h5>Không có đơn nào</h5>
          <p>Hãy đặt sân để bắt đầu!</p>
          <a href="danh-sach-san.html" class="btn btn-primary">Đặt sân ngay</a>
        </div>`;
        return;
    }
    wrap.innerHTML = data.map(don => {
        const sc   = statusClass(don.trangThai);
        const icon = statusIcon(sc);
        const ngay = don.ngayDa ? formatDate2(don.ngayDa) : '—';
        const gio  = [(don.gioBatDau||'').substring(0,5), (don.gioKetThuc||'').substring(0,5)].filter(Boolean).join(' - ');
        // Chỉ cho hủy khi đang "Chờ duyệt"
        const canCancel = don.trangThai === 'Chờ duyệt';
        return `
      <div class="history-card">
        <div class="history-icon ${sc}">${icon}</div>
        <div class="flex-grow-1">
          <div class="d-flex justify-content-between align-items-start flex-wrap gap-2">
            <div>
              <h6 class="fw-bold mb-1">${don.tenSan || '—'}</h6>
              <p class="text-muted mb-1" style="font-size:14px">
                <i class="fas fa-calendar me-1"></i>${ngay}
                &nbsp;|&nbsp;
                <i class="fas fa-clock me-1"></i>${gio}
              </p>
              <p class="text-muted mb-0" style="font-size:13px">Mã đơn: <code>${don.maDon}</code></p>
            </div>
            <div class="text-end">
              <div class="badge-tt ${sc} mb-1">${don.trangThai || '—'}</div>
              <div class="fw-bold text-primary">${formatCurrency(don.tienSan)}</div>
              ${don.diemThuong ? `<div class="text-success" style="font-size:12px">+${don.diemThuong} điểm</div>` : ''}
            </div>
          </div>
          ${canCancel ? `<button class="btn btn-outline-danger btn-sm mt-2" onclick="huyDon('${don.maDon}')"><i class="fas fa-times me-1"></i>Hủy đơn</button>` : ''}
        </div>
      </div>`;
    }).join('');
}

async function huyDon(maDon) {
    if (!confirm('Xác nhận hủy đơn ' + maDon + '?')) return;
    try {
        const res = await fetch(
            API_BASE + '/api/dondatsan/' + maDon + '/trang-thai?trangThai=' + encodeURIComponent('Đã hủy'),
            { method: 'PATCH' }
        );
        if (!res.ok) throw new Error();
        const idx = allDons.findIndex(d => d.maDon === maDon);
        if (idx >= 0) allDons[idx].trangThai = 'Đã hủy';
        updateStats();
        renderList();
    } catch {
        alert('Không hủy được đơn!');
    }
}

// statusClass dùng giá trị chuỗi chính xác từ DB
function statusClass(tt) {
    if (tt === 'Đã xác nhận')   return 'confirmed';
    if (tt === 'Đã hoàn thành') return 'done';
    if (tt === 'Đã hủy')        return 'cancelled';
    return 'pending'; // Chờ duyệt
}

function statusIcon(cls) {
    const map = {
        confirmed: '<i class="fas fa-check"></i>',
        done:      '<i class="fas fa-trophy"></i>',
        cancelled: '<i class="fas fa-times"></i>',
        pending:   '<i class="fas fa-clock"></i>'
    };
    return map[cls] || map.pending;
}

function formatDate2(d) {
    if (!d) return '—';
    const p = String(d).split('-');
    return p.length === 3 ? p[2]+'/'+p[1]+'/'+p[0] : d;
}
