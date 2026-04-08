// API được khai báo trong script.js
let allDons    = [];
let filtered   = [];
let currentPage = 1;
const PAGE_SIZE = 10;
let viewingDon  = null;

document.addEventListener('DOMContentLoaded', async () => {
    await loadDons();
    await loadSansForCreate();
    document.getElementById('c-ngay').value = new Date().toISOString().split('T')[0];
});

async function loadDons() {
    try {
        const res = await fetch(API + '/api/dondatsan');
        if (!res.ok) throw new Error('HTTP ' + res.status);
        allDons = await res.json();
        allDons.sort((a,b) => new Date(b.ngayDat||0) - new Date(a.ngayDat||0));
        updateStatCards();
        applyFilter();
    } catch(e) {
        document.getElementById('don-tbody').innerHTML =
            '<tr><td colspan="7" class="text-center text-danger py-4"><i class="fas fa-wifi-slash me-2"></i>Lỗi kết nối server! Kiểm tra backend đã chạy chưa.</td></tr>';
    }
}

// Đúng với DB: Chờ duyệt | Đã xác nhận | Đã hoàn thành | Đã hủy
function updateStatCards() {
    document.getElementById('cnt-cho').textContent  = allDons.filter(d => d.trangThai === 'Chờ duyệt').length;
    document.getElementById('cnt-xn').textContent   = allDons.filter(d => d.trangThai === 'Đã xác nhận').length;
    document.getElementById('cnt-ht').textContent   = allDons.filter(d => d.trangThai === 'Đã hoàn thành').length;
    document.getElementById('cnt-huy').textContent  = allDons.filter(d => d.trangThai === 'Đã hủy').length;
}

function applyFilter() {
    const tt = document.getElementById('filter-tt').value;
    const q  = (document.getElementById('search-don').value||'').toLowerCase();
    filtered = allDons.filter(d => {
        const matchTT = !tt || d.trangThai === tt;
        const matchQ  = !q  || (d.maDon||'').toLowerCase().includes(q)
                             || (d.tenKhachHang||'').toLowerCase().includes(q)
                             || (d.sdtKhachHang||'').includes(q)
                             || (d.tenSan||'').toLowerCase().includes(q);
        return matchTT && matchQ;
    });
    document.getElementById('don-count').textContent = filtered.length;
    currentPage = 1;
    renderTable();
}

function renderTable() {
    const total  = filtered.length;
    const pages  = Math.ceil(total / PAGE_SIZE);
    const start  = (currentPage - 1) * PAGE_SIZE;
    const data   = filtered.slice(start, start + PAGE_SIZE);
    document.getElementById('page-info').textContent =
        total === 0 ? 'Không có đơn nào' : `Hiển thị ${start+1}–${Math.min(start+data.length,total)} / ${total} đơn`;

    const tbody = document.getElementById('don-tbody');
    tbody.innerHTML = data.length === 0
        ? '<tr><td colspan="7" class="text-center text-muted py-4">Không có đơn phù hợp.</td></tr>'
        : data.map(d => {
            const ttClass = statusClass(d.trangThai);
            const ngay    = d.ngayDa ? fmtDate(d.ngayDa) : '—';
            const gio     = [(d.gioBatDau||'').substring(0,5),(d.gioKetThuc||'').substring(0,5)].join(' - ');
            const actions = buildActions(d);
            return `<tr>
              <td class="ps-4"><strong class="text-primary font-monospace" style="cursor:pointer" onclick="viewDon('${d.maDon}')">${d.maDon}</strong></td>
              <td>
                <div class="fw-bold">${d.tenKhachHang||'—'}</div>
                <div class="small text-muted">${d.sdtKhachHang||d.maKH||'—'}</div>
              </td>
              <td>
                <div>${d.tenSan||d.maSan||'—'}</div>
                <div class="small text-primary"><i class="fas fa-calendar me-1"></i>${ngay} &nbsp;<i class="fas fa-clock me-1"></i>${gio}</div>
              </td>
              <td class="text-danger fw-bold">${fmtMoney(d.tienSan)}</td>
              <td class="text-success fw-bold">${fmtMoney(d.tienCoc)}</td>
              <td><span class="status-badge ${ttClass}">${d.trangThai||'—'}</span></td>
              <td class="text-end pe-4">${actions}</td>
            </tr>`;
        }).join('');

    renderPagination(pages);
}

function buildActions(d) {
    const tt  = (d.trangThai||'');
    let html  = `<button class="btn btn-light btn-sm me-1" onclick="viewDon('${d.maDon}')" title="Chi tiết"><i class="fas fa-eye"></i></button>`;
    if (tt === 'Chờ duyệt') {
        html += `<button class="btn btn-success btn-sm me-1" onclick="updateTT('${d.maDon}','Đã xác nhận')" title="Duyệt"><i class="fas fa-check"></i> Duyệt</button>`;
        html += `<button class="btn btn-outline-danger btn-sm" onclick="updateTT('${d.maDon}','Đã hủy')" title="Hủy"><i class="fas fa-times"></i> Hủy</button>`;
    } else if (tt === 'Đã xác nhận') {
        html += `<button class="btn btn-primary btn-sm me-1" onclick="updateTT('${d.maDon}','Đã hoàn thành')" title="Hoàn thành"><i class="fas fa-flag-checkered"></i> Xong</button>`;
        html += `<button class="btn btn-outline-danger btn-sm" onclick="updateTT('${d.maDon}','Đã hủy')" title="Hủy"><i class="fas fa-times"></i> Hủy</button>`;
    }
    return html;
}

function statusClass(tt) {
    if (!tt) return 'status-cho';
    const t = tt;
    if (t === 'Chờ duyệt')     return 'status-cho';
    if (t === 'Đã xác nhận')   return 'status-xacnhan';
    if (t === 'Đã hoàn thành') return 'status-hoantat';
    if (t === 'Đã hủy')        return 'status-huy';
    return 'status-cho';
}

function renderPagination(pages) {
    const pag = document.getElementById('pagination');
    if (pages <= 1) { pag.innerHTML = ''; return; }
    let html = '';
    html += `<button class="btn btn-sm btn-outline-secondary" onclick="goPage(${currentPage-1})" ${currentPage===1?'disabled':''}><i class="fas fa-chevron-left"></i></button>`;
    for (let p = 1; p <= pages; p++) {
        if (pages > 7 && p > 2 && p < pages-1 && Math.abs(p-currentPage) > 1) {
            if (p===3||p===pages-2) html += `<button class="btn btn-sm btn-outline-secondary" disabled>…</button>`;
            continue;
        }
        html += `<button class="btn btn-sm ${p===currentPage?'btn-primary':'btn-outline-secondary'}" onclick="goPage(${p})">${p}</button>`;
    }
    html += `<button class="btn btn-sm btn-outline-secondary" onclick="goPage(${currentPage+1})" ${currentPage===pages?'disabled':''}><i class="fas fa-chevron-right"></i></button>`;
    pag.innerHTML = html;
}

function goPage(p) {
    const pages = Math.ceil(filtered.length / PAGE_SIZE);
    if (p < 1 || p > pages) return;
    currentPage = p;
    renderTable();
}

async function updateTT(maDon, trangThai) {
    const confirm_msg = trangThai === 'Đã hủy'
        ? `Xác nhận HỦY đơn ${maDon}?`
        : `Xác nhận cập nhật đơn ${maDon} → "${trangThai}"?`;
    const ok = await askConfirm(confirm_msg);
    if (!ok) return;
    try {
        const res = await fetch(
            `${API}/api/dondatsan/${encodeURIComponent(maDon)}/trang-thai?trangThai=${encodeURIComponent(trangThai)}`,
            { method: 'PATCH' }
        );
        if (!res.ok) throw new Error(await res.text());
        const updated = await res.json();
        const idx = allDons.findIndex(d => d.maDon === maDon);
        if (idx >= 0) allDons[idx] = updated;
        updateStatCards();
        applyFilter();
        showToast(`Đã cập nhật đơn ${maDon} → ${trangThai}`, trangThai === 'Đã hủy' ? 'danger' : 'success');
        if (viewingDon && viewingDon.maDon === maDon) viewDon(maDon);
    } catch(e) {
        showToast('Lỗi: ' + (e.message||'Vui lòng thử lại!'), 'danger');
    }
}

function viewDon(maDon) {
    const d = allDons.find(x => x.maDon === maDon);
    if (!d) return;
    viewingDon = d;
    document.getElementById('d-ma').textContent      = d.maDon;
    document.getElementById('d-kh').textContent      = (d.tenKhachHang||'—') + (d.sdtKhachHang ? ' · ' + d.sdtKhachHang : '');
    document.getElementById('d-san').textContent     = d.tenSan || d.maSan || '—';
    document.getElementById('d-ngay').textContent    = d.ngayDa ? fmtDate(d.ngayDa) : '—';
    document.getElementById('d-gio').textContent     = [(d.gioBatDau||'').substring(0,5),(d.gioKetThuc||'').substring(0,5)].join(' – ');
    document.getElementById('d-tien').textContent    = fmtMoney(d.tienSan);
    document.getElementById('d-coc').textContent     = fmtMoney(d.tienCoc);
    document.getElementById('d-diem').textContent    = (d.diemThuong||0) + ' điểm';
    document.getElementById('d-tt').innerHTML        = `<span class="status-badge ${statusClass(d.trangThai)}">${d.trangThai||'—'}</span>`;
    document.getElementById('d-ngaydat').textContent = d.ngayDat ? new Date(d.ngayDat).toLocaleString('vi-VN') : '—';

    const actDiv = document.getElementById('d-actions');
    actDiv.innerHTML = '';
    const tt = d.trangThai || '';
    if (tt === 'Chờ duyệt') {
        actDiv.innerHTML = `
          <button class="btn btn-success flex-fill" onclick="updateTT('${d.maDon}','Đã xác nhận');closeDetail()">
            <i class="fas fa-check me-1"></i>Duyệt đơn
          </button>
          <button class="btn btn-outline-danger flex-fill" onclick="updateTT('${d.maDon}','Đã hủy');closeDetail()">
            <i class="fas fa-times me-1"></i>Hủy
          </button>`;
    } else if (tt === 'Đã xác nhận') {
        actDiv.innerHTML = `
          <button class="btn btn-primary flex-fill" onclick="updateTT('${d.maDon}','Đã hoàn thành');closeDetail()">
            <i class="fas fa-flag-checkered me-1"></i>Hoàn thành
          </button>
          <button class="btn btn-outline-danger flex-fill" onclick="updateTT('${d.maDon}','Đã hủy');closeDetail()">
            <i class="fas fa-times me-1"></i>Hủy
          </button>`;
    } else {
        actDiv.innerHTML = `<button class="btn btn-secondary flex-fill" onclick="closeDetail()">Đóng</button>`;
    }

    document.getElementById('detailModal').classList.add('show');
}

function closeDetail() {
    document.getElementById('detailModal').classList.remove('show');
    viewingDon = null;
}
document.getElementById('detailModal').addEventListener('click', e => { if (e.target === e.currentTarget) closeDetail(); });

async function loadSansForCreate() {
    try {
        const res  = await fetch(API + '/api/san');
        const sans = await res.json();
        const sel  = document.getElementById('c-san');
        sel.innerHTML = '<option value="">-- Chọn sân --</option>';
        sans.filter(s => (s.trangThai||'').toUpperCase() !== 'MAINTENANCE')
            .forEach(s => {
                const opt = document.createElement('option');
                opt.value = s.maSan;
                opt.textContent = s.tenSan + ' – ' + fmtMoney(s.giaThue) + '/1.5h';
                sel.appendChild(opt);
            });
    } catch {}
}

async function submitCreate() {
    const err   = document.getElementById('create-err');
    const btn   = document.getElementById('btn-create');
    const maKH  = document.getElementById('c-makh').value.trim();
    const maSan = document.getElementById('c-san').value;
    const ngay  = document.getElementById('c-ngay').value;
    const bd    = document.getElementById('c-bd').value;
    const kt    = document.getElementById('c-kt').value;
    const km    = document.getElementById('c-km').value.trim();
    err.classList.add('d-none');
    if (!maKH||!maSan||!ngay||!bd||!kt) {
        err.textContent = 'Vui lòng điền đầy đủ thông tin!';
        err.classList.remove('d-none'); return;
    }
    if (bd >= kt) {
        err.textContent = 'Giờ bắt đầu phải nhỏ hơn giờ kết thúc!';
        err.classList.remove('d-none'); return;
    }
    const dto = { maKH, maSan, ngayDa: ngay, gioBatDau: bd, gioKetThuc: kt };
    if (km) dto.maCodeKhuyenMai = km;
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang tạo...';
    try {
        const res = await fetch(API + '/api/dondatsan', {
            method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(dto)
        });
        if (!res.ok) { const m = await res.text(); throw new Error(m); }
        const newDon = await res.json();
        allDons.unshift(newDon);
        updateStatCards();
        applyFilter();
        bootstrap.Modal.getInstance(document.getElementById('createModal')).hide();
        showToast('Tạo đơn ' + newDon.maDon + ' thành công!', 'success');
    } catch(e) {
        err.textContent = 'Lỗi: ' + (e.message||'Vui lòng thử lại!');
        err.classList.remove('d-none');
    } finally {
        btn.disabled = false;
        btn.innerHTML = '<i class="fas fa-save me-1"></i>Tạo đơn';
    }
}

// Helpers
function fmtDate(d) {
    if (!d) return '—';
    const p = String(d).split('-');
    return p.length === 3 ? `${p[2]}/${p[1]}/${p[0]}` : d;
}
function fmtMoney(n) {
    if (n == null) return '—';
    return Number(n).toLocaleString('vi-VN') + 'đ';
}
function showToast(msg, type='success') {
    const t = document.createElement('div');
    t.style.cssText = 'position:fixed;top:20px;right:20px;z-index:9999;min-width:280px';
    t.innerHTML = `<div class="alert alert-${type==='success'?'success':'danger'} shadow mb-0">${msg}</div>`;
    document.body.appendChild(t);
    setTimeout(() => t.remove(), 3000);
}
function askConfirm(msg) {
    return new Promise(resolve => {
        const modalEl = document.getElementById('confirmActionModal');
        const okBtn   = document.getElementById('confirmActionOk');
        document.getElementById('confirmActionText').textContent = msg;
        const modal   = new bootstrap.Modal(modalEl);
        const handleOk  = () => { cleanup(); resolve(true);  modal.hide(); };
        const handleHide = () => { cleanup(); resolve(false); };
        function cleanup() {
            okBtn.removeEventListener('click', handleOk);
            modalEl.removeEventListener('hidden.bs.modal', handleHide);
        }
        okBtn.addEventListener('click', handleOk, { once: true });
        modalEl.addEventListener('hidden.bs.modal', handleHide, { once: true });
        modal.show();
    });
}
