const API = 'http://localhost:8080';
let allSans   = [];
let loaiList  = [];
let availableAnh = [];
let editMaSan = null;
let deleteMaSan = null;
const PAGE_SIZE_SAN = 10;
let currentPageSan  = 1;
let filteredSans    = [];

document.addEventListener('DOMContentLoaded', async () => {
  await loadLoaiSan();
  await loadAnhSan();
  await loadSans();
});

async function loadLoaiSan() {
  try {
    const res = await fetch(API + '/api/loaisan');
    loaiList  = await res.json();
    const opts = loaiList.map(ls => `<option value="${ls.maLoaiSan}">${ls.tenLoaiSan||'Sân '+ls.soNguoi+' người'}</option>`).join('');
    ['add-loai','edit-loai'].forEach(id => {
      const el = document.getElementById(id);
      if (el) el.innerHTML = '<option value="">-- Chọn loại sân --</option>' + opts;
    });
    const filter = document.getElementById('filter-loai-admin');
    if (filter) filter.innerHTML = '<option value="">Tất cả loại</option>' + opts;
  } catch(e) { console.warn(e); }
}

async function loadAnhSan() {
  try {
    const res = await fetch(API + '/api/anhsan');
    availableAnh = await res.json();
    renderAnhOptions();
  } catch(e) {
    console.warn('Không thể tải ảnh sân có sẵn', e);
  }
}

function renderAnhOptions() {
  const opts = ['<option value="">-- Chọn ảnh có sẵn --</option>']
    .concat(availableAnh.map(a => `<option value="${a.duongDanAnh}">${a.maAnh} — ${a.duongDanAnh}</option>`))
    .join('');
  ['add-anh','edit-anh'].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.innerHTML = opts;
  });
}

function updateAnhPreview(selectId) {
  const select = document.getElementById(selectId);
  const preview = document.getElementById(selectId + '-preview');
  if (!select || !preview) return;
  const value = select.value;
  if (!value) {
    preview.style.display = 'none';
    preview.src = '';
    return;
  }
  preview.src = value;
  preview.style.display = 'block';
}

async function loadSans() {
  try {
    const res = await fetch(API + '/api/san');
    allSans   = await res.json();
    applyFilter();
  } catch {
    document.getElementById('san-tbody').innerHTML = '<tr><td colspan="7" class="text-center text-danger py-4">Lỗi kết nối server!</td></tr>';
  }
}

function applyFilter() {
  const q    = (document.getElementById('search-input').value || '').toLowerCase();
  const loai = document.getElementById('filter-loai-admin').value;
  filteredSans = allSans.filter(s => {
    const matchQ = !q || (s.tenSan||'').toLowerCase().includes(q) || (s.maSan||'').toLowerCase().includes(q);
    const matchL = !loai || s.maLoaiSan === loai;
    return matchQ && matchL;
  });
  document.getElementById('san-count').textContent = filteredSans.length;
  currentPageSan = 1;
  renderPage();
}

function renderPage() {
  const total  = filteredSans.length;
  const pages  = Math.ceil(total / PAGE_SIZE_SAN);
  const start  = (currentPageSan - 1) * PAGE_SIZE_SAN;
  const data   = filteredSans.slice(start, start + PAGE_SIZE_SAN);
  document.getElementById('san-info').textContent =
    total === 0 ? 'Không có sân nào' :
    `Hiển thị ${start+1}–${Math.min(start+data.length, total)} / ${total} sân`;
  renderTable(data);
  renderPagination('san-pagination', currentPageSan, pages, (p) => { currentPageSan = p; renderPage(); });
}

function renderTable(data) {
  const statusMap = { EMPTY:'badge-soft-success', ORDERD:'badge-soft-danger', MAINTENANCE:'badge-soft-warning' };
  const labelMap  = { EMPTY:'Còn trống', ORDERD:'Đang dùng', MAINTENANCE:'Bảo trì' };
  document.getElementById('san-tbody').innerHTML = data.length === 0
    ? '<tr><td colspan="7" class="text-center text-muted py-4">Không có sân nào.</td></tr>'
    : data.map(s => `
      <tr>
        <td class="ps-4"><code>${s.maSan}</code></td>
        <td>
          <div class="d-flex align-items-center gap-2">
            ${s.anhChinh ? `<img src="${s.anhChinh}" style="width:40px;height:30px;object-fit:cover;border-radius:4px" onerror="this.style.display='none'">` : ''}
            <strong>${s.tenSan}</strong>
          </div>
        </td>
        <td>${s.tenLoaiSan||'—'}</td>
        <td>${s.diaChi||'—'}</td>
        <td><strong>${Number(s.giaThue||0).toLocaleString('vi-VN')}đ</strong></td>
        <td><span class="badge-soft ${statusMap[s.trangThai]||'badge-soft-secondary'}">${labelMap[s.trangThai]||s.trangThai||'—'}</span></td>
        <td class="text-end pe-4">
          <button class="btn btn-icon btn-edit me-1" onclick="openEdit('${s.maSan}')" title="Sửa"><i class="fas fa-pen"></i></button>
          <button class="btn btn-icon btn-delete" onclick="openDelete('${s.maSan}','${s.tenSan}')" title="Xóa"><i class="fas fa-trash"></i></button>
        </td>
      </tr>`).join('');
}

function resetAddForm() {
  ['add-ten','add-diachi','add-gia','add-mota','add-tieních'].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.value = '';
  });
  const addAnh = document.getElementById('add-anh');
  if (addAnh) addAnh.selectedIndex = 0;
  updateAnhPreview('add-anh');
  document.getElementById('add-loai').selectedIndex = 0;
  document.getElementById('add-error').classList.add('d-none');
}

async function saveSan() {
  const ten  = document.getElementById('add-ten').value.trim();
  const loai = document.getElementById('add-loai').value;
  const gia  = parseFloat(document.getElementById('add-gia').value);
  const err  = document.getElementById('add-error');
  err.classList.add('d-none');
  if (!ten || !loai || !gia) { err.textContent = 'Vui lòng điền tên sân, loại sân và giá thuê!'; err.classList.remove('d-none'); return; }
  const dto = {
    tenSan:   ten,
    diaChi:   document.getElementById('add-diachi').value.trim(),
    maLoaiSan:loai,
    giaThue:  gia,
    moTa:     document.getElementById('add-mota').value.trim(),
    tienIch:  document.getElementById('add-tieních').value.trim(),
    anhChinh: document.getElementById('add-anh').value.trim(),
    trangThai:'EMPTY'
  };
  const btn = document.getElementById('btn-add-save');
  btn.disabled = true; btn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang lưu...';
  try {
    const res = await fetch(API + '/api/san', { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(dto) });
    if (!res.ok) { const msg = await res.text(); throw new Error(msg); }
    const newSan = await res.json();
    allSans.push(newSan);
    applyFilter();
    bootstrap.Modal.getInstance(document.getElementById('addPitchModal')).hide();
    showToast('Thêm sân thành công!', 'success');
  } catch(e) { err.textContent = 'Lỗi: ' + (e.message||'Vui lòng thử lại!'); err.classList.remove('d-none'); }
  finally { btn.disabled = false; btn.innerHTML = '<i class="fas fa-save me-1"></i>Lưu lại'; }
}

function openEdit(maSan) {
  const san = allSans.find(s => s.maSan === maSan);
  if (!san) return;
  editMaSan = maSan;
  document.getElementById('edit-ma').value     = san.maSan;
  document.getElementById('edit-ten').value    = san.tenSan || '';
  document.getElementById('edit-diachi').value = san.diaChi || '';
  document.getElementById('edit-gia').value    = san.giaThue || '';
  document.getElementById('edit-mota').value   = san.moTa || '';
  document.getElementById('edit-tieních').value= san.tienIch || '';
  document.getElementById('edit-anh').value    = san.anhChinh || '';
  updateAnhPreview('edit-anh');
  document.getElementById('edit-tt').value     = san.trangThai || 'EMPTY';
  const loaiEl = document.getElementById('edit-loai');
  if (loaiEl) loaiEl.value = san.maLoaiSan || '';
  document.getElementById('edit-error').classList.add('d-none');
  new bootstrap.Modal(document.getElementById('editPitchModal')).show();
}

async function updateSan() {
  const err = document.getElementById('edit-error');
  err.classList.add('d-none');
  const dto = {
    tenSan:    document.getElementById('edit-ten').value.trim(),
    diaChi:    document.getElementById('edit-diachi').value.trim(),
    maLoaiSan: document.getElementById('edit-loai').value,
    giaThue:   parseFloat(document.getElementById('edit-gia').value),
    moTa:      document.getElementById('edit-mota').value.trim(),
    tienIch:   document.getElementById('edit-tieních').value.trim(),
    anhChinh:  document.getElementById('edit-anh').value.trim(),
    trangThai: document.getElementById('edit-tt').value
  };
  const btn = document.getElementById('btn-edit-save');
  btn.disabled = true; btn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang lưu...';
  try {
    const res = await fetch(API + '/api/san/' + editMaSan, { method:'PUT', headers:{'Content-Type':'application/json'}, body:JSON.stringify(dto) });
    if (!res.ok) throw new Error(await res.text());
    const updated = await res.json();
    const idx = allSans.findIndex(s => s.maSan === editMaSan);
    if (idx >= 0) allSans[idx] = updated;
    applyFilter();
    bootstrap.Modal.getInstance(document.getElementById('editPitchModal')).hide();
    showToast('Cập nhật sân thành công!', 'success');
  } catch(e) { err.textContent = 'Lỗi: ' + (e.message||'Vui lòng thử lại!'); err.classList.remove('d-none'); }
  finally { btn.disabled = false; btn.innerHTML = '<i class="fas fa-save me-1"></i>Cập nhật'; }
}

function openDelete(maSan, tenSan) {
  deleteMaSan = maSan;
  document.getElementById('delete-san-name').textContent = tenSan;
  document.getElementById('btn-confirm-delete').onclick = confirmDelete;
  new bootstrap.Modal(document.getElementById('deleteModal')).show();
}

async function confirmDelete() {
  const btn = document.getElementById('btn-confirm-delete');
  btn.disabled = true; btn.textContent = 'Đang xóa...';
  try {
    const res = await fetch(API + '/api/san/' + deleteMaSan, { method:'DELETE' });
    if (!res.ok) {
      const raw = await res.text();
      let msg = raw;
      try { msg = JSON.parse(raw).message || raw; } catch {}
      throw new Error(msg);
    }
    allSans = allSans.filter(s => s.maSan !== deleteMaSan);
    applyFilter();
    bootstrap.Modal.getInstance(document.getElementById('deleteModal')).hide();
    showToast('Đã xóa sân thành công!', 'success');
  } catch(e) {
    bootstrap.Modal.getInstance(document.getElementById('deleteModal'))?.hide();
    showToast('⚠️ ' + (e.message||'Vui lòng thử lại!'), 'danger');
  }
  finally { btn.disabled = false; btn.textContent = 'Xóa'; }
}

function showToast(msg, type) {
  const t = document.createElement('div');
  t.style.cssText = 'position:fixed;top:20px;right:20px;z-index:9999;min-width:260px';
  t.innerHTML = `<div class="alert alert-${type==='success'?'success':'danger'} shadow-sm mb-0">${msg}</div>`;
  document.body.appendChild(t);
  setTimeout(() => t.remove(), 3000);
}
