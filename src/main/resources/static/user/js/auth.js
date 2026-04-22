function resolveApiBase() {
    if (typeof window === 'undefined' || !window.location) return 'http://localhost:8080';
    const href = window.location.href || '';
    const origin = window.location.origin;
    if (!origin || origin === 'null' || href.startsWith('file:')) {
        return 'http://localhost:8080';
    }
    // Nếu đang mở qua Live Server hoặc bất kỳ port nào khác 8080
    // → vẫn phải trỏ về Spring Boot đang chạy ở port 8080
    const port = window.location.port;
    if (port && port !== '8080') {
        return window.location.protocol + '//' + window.location.hostname + ':8080';
    }
    return origin;
}

const API_BASE = resolveApiBase();

const SAN_IMG_FALLBACK = 'https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=400&q=80';

/** Tên file trong DB (san1.jpg) → URL tĩnh /images/san/... */
function resolveSanImageUrl(raw) {
    if (!raw || !String(raw).trim()) return SAN_IMG_FALLBACK;
    const t = String(raw).trim();
    if (t.startsWith('http://') || t.startsWith('https://')) return t;
    if (t.startsWith('/')) return t;
    return '/images/san/' + t;
}

function getCurrentUser() {
    try { return JSON.parse(localStorage.getItem('arenax_user')); }
    catch { return null; }
}
function setCurrentUser(user) { localStorage.setItem('arenax_user', JSON.stringify(user)); }
function logout() { localStorage.removeItem('arenax_user'); window.location.href = 'login.html'; }

function getRankBadge(hang) {
    const h = (hang || '').toLowerCase();
    if (h.includes('v\u00e0ng')) return `<i class="fas fa-crown" style="color:#f5c518;"></i> V\u00e0ng`;
    if (h.includes('b\u1ea1c')) return `<i class="fas fa-medal" style="color:#b0c4de;"></i> B\u1ea1c`;
    return `<i class="fas fa-medal" style="color:#cd7f32;"></i> \u0110\u1ed3ng`;
}

function renderNavbar() {
    const user = getCurrentUser();
    const loginItem = document.getElementById('nav-login-item');
    const userItem  = document.getElementById('nav-user-item');
    if (!loginItem || !userItem) return;
    if (user) {
        // Admin xem trang user bình thường, không redirect
        loginItem.style.display = 'none';
        userItem.style.display  = 'block';
        const nameEl  = document.getElementById('nav-user-name');
        const pointEl = document.getElementById('nav-user-points');
        const rankEl  = document.getElementById('nav-user-rank');
        if (nameEl)  nameEl.textContent = user.hoTen || 'Kh\u00e1ch';
        if (pointEl) pointEl.textContent = (user.diemTichLuy || 0) + ' \u0111i\u1ec3m';
        if (rankEl)  rankEl.innerHTML   = getRankBadge(user.hangThanhVien);
    } else {
        loginItem.style.display = 'block';
        userItem.style.display  = 'none';
    }
}

function requireLogin(redirectUrl) {
    if (!getCurrentUser()) {
        if (redirectUrl) sessionStorage.setItem('arenax_redirect', redirectUrl);
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

function formatCurrency(amount) {
    if (amount == null) return '\u2014';
    return Number(amount).toLocaleString('vi-VN') + '\u0111';
}

async function loadLoaiSan(selectEl) {
    try {
        const res = await fetch(`${API_BASE}/api/loaisan`);
        if (!res.ok) return;
        const list = await res.json();
        selectEl.innerHTML = '<option value="">T\u1ea5t c\u1ea3 lo\u1ea1i s\u00e2n</option>';
        list.forEach(ls => {
            const opt = document.createElement('option');
            opt.value = ls.maLoaiSan;
            opt.textContent = ls.tenLoaiSan || `S\u00e2n ${ls.soNguoi} ng\u01b0\u1eddi`;
            selectEl.appendChild(opt);
        });
    } catch(e) { console.warn(e); }
}

function buildSanCard(san) {
    const isAvail = (san.trangThai || '').toUpperCase() !== 'MAINTENANCE';
    const badgeTT = isAvail
        ? `<span class="badge bg-success"><i class="fas fa-check-circle me-1"></i>C\u00f2n tr\u1ed1ng</span>`
        : `<span class="badge bg-danger"><i class="fas fa-times-circle me-1"></i>B\u1ea3o tr\u00ec</span>`;
    const btnDat = isAvail
        ? `<button class="btn btn-primary w-100 fw-bold" onclick="datSan('${san.maSan}')">ĐẶT NGAY</button>`
        : `<button class="btn btn-secondary w-100 fw-bold" disabled>ĐANG BẢO TRÌ</button>`;
    const img  = resolveSanImageUrl(san.anhChinh);
    const loai = san.tenLoaiSan || (san.soNguoi ? `S\u00e2n ${san.soNguoi} ng\u01b0\u1eddi` : 'S\u00e2n b\u00f3ng');
    return `
    <div class="col-md-4 mb-4">
      <div class="card pitch-card h-100">
        <img src="${img}" class="pitch-img card-img-top" alt="${san.tenSan}"
             onerror="this.src='https://images.unsplash.com/photo-1574629810360-7efbbe195018?w=400&q=80'">
        <div class="card-body">
          <div class="d-flex justify-content-between mb-2">
            <span class="badge bg-info text-dark">${loai}</span>${badgeTT}
          </div>
          <h5 class="card-title fw-bold">${san.tenSan}</h5>
          <p class="card-text text-muted"><i class="fas fa-map-marker-alt me-2"></i>${san.diaChi || ''}</p>
          <div class="d-flex justify-content-between align-items-center mt-3">
            <div class="price-tag">${formatCurrency(san.giaThue)}<span class="text-muted fs-6 fw-normal">/1.5h</span></div>
            <button class="btn btn-outline-primary btn-sm" onclick="xemChiTiet('${san.maSan}')">Xem chi ti\u1ebft</button>
          </div>
        </div>
        <div class="card-footer bg-white border-0 pb-3">${btnDat}</div>
      </div>
    </div>`;
}

function datSan(maSan) {
    if (!requireLogin()) return;
    sessionStorage.setItem('arenax_maSan', maSan);
    window.location.href = 'thanh-toan.html';
}

async function xemChiTiet(maSan) {
    try {
        const res = await fetch(`${API_BASE}/api/san/${maSan}`);
        if (!res.ok) throw new Error();
        const san = await res.json();
        const img  = resolveSanImageUrl(san.anhChinh);
        const loai = san.tenLoaiSan || `S\u00e2n ${san.soNguoi} ng\u01b0\u1eddi`;
        let tienIchHtml = '';
        if (san.tienIch) {
            san.tienIch.split(',').forEach(ti => {
                tienIchHtml += `<div class="amenity"><i class="fas fa-check-circle text-success me-1"></i>${ti.trim()}</div>`;
            });
        }
        document.getElementById('modal-san-title').textContent  = san.tenSan;
        document.getElementById('modal-san-diachi').textContent = san.diaChi || '';
        document.getElementById('modal-san-loai').textContent   = loai;
        document.getElementById('modal-san-mota').textContent   = san.moTa || 'Ch\u01b0a c\u00f3 m\u00f4 t\u1ea3.';
        document.getElementById('modal-san-tieních').innerHTML  = tienIchHtml || '<span class="text-muted">Ch\u01b0a c\u1eadp nh\u1eadt</span>';
        document.getElementById('modal-san-gia').textContent    = formatCurrency(san.giaThue);
        document.getElementById('modal-san-img').src            = img;
        document.getElementById('modal-btn-dat').onclick        = () => { closeModal(); datSan(maSan); };
        document.getElementById('detailModal').style.display    = 'flex';
    } catch { alert('Kh\u00f4ng t\u1ea3i \u0111\u01b0\u1ee3c th\u00f4ng tin s\u00e2n!'); }
}

function closeModal() {
    const m = document.getElementById('detailModal');
    if (m) m.style.display = 'none';
}

document.addEventListener('DOMContentLoaded', renderNavbar);
