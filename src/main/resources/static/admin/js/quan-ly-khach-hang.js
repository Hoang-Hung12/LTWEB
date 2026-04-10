/* API khai báo trong script.js */
let allKH   = [];
let editMaTK = null;
let deleteMaTK = null;

const PAGE_SIZE = 10;
let currentPage = 1;

document.addEventListener('DOMContentLoaded', loadKH);

async function loadKH() {
  try {
    const res = await fetch(API + '/api/taikhoan');
    const all = await res.json();
    allKH = all.filter(u => (u.vaiTro||'').toUpperCase() !== 'ADMIN');
    applyFilter();
  } catch {
    document.getElementById('kh-tbody').innerHTML = '<tr><td colspan="8" class="text-center text-danger py-4">Lỗi kết nối server!</td></tr>';
  }
}

function applyFilter() {
  const q    = (document.getElementById('search-kh').value||'').toLowerCase();
  const hang = document.getElementById('filter-hang').value;
  const data = allKH.filter(k => {
    const matchQ = !q ||
      (k.maTK||'').toLowerCase().includes(q) ||
      (k.hoTen||'').toLowerCase().includes(q) ||
      (k.sdt||'').includes(q) ||
      (k.email||'').toLowerCase().includes(q);
    const matchH = !hang || k.hangThanhVien === hang;
    return matchQ && matchH;
  });
  document.getElementById('kh-count').textContent = data.length;
  currentPage = 1;
  renderTable(data);
}

function renderTable(data) {
  const start = (currentPage-1)*PAGE_SIZE;
  const page  = data.slice(start, start+PAGE_SIZE);
  document.getElementById('kh-info').textContent = `Hiển thị ${start+1}–${Math.min(start+page.length,data.length)} / ${data.length} khách hàng`;
  const rankIcon = h => {
    if (!h) return '';
    const l = h.toLowerCase();
    if (l.includes('vàng')||l.includes('vang')) return `<i class="fas fa-crown rank-gold me-1"></i>`;
    if (l.includes('bạc')||l.includes('bac'))   return `<i class="fas fa-medal rank-silver me-1"></i>`;
    return `<i class="fas fa-medal rank-bronze me-1"></i>`;
  };
  document.getElementById('kh-tbody').innerHTML = page.length === 0
    ? '<tr><td colspan="8" class="text-center text-muted py-4">Không có khách hàng nào.</td></tr>'
    : page.map((k, i) => `
      <tr>
        <td class="ps-4">${start+i+1}</td>
        <td><code>${k.maTK||'—'}</code></td>
        <td class="fw-bold">${k.hoTen||'—'}</td>
        <td>${k.sdt||'—'}</td>
        <td>${k.email||'—'}</td>
        <td><strong class="text-primary">${(k.diemTichLuy||0).toLocaleString('vi-VN')}</strong></td>
        <td>${rankIcon(k.hangThanhVien)}${k.hangThanhVien||'Đồng'}</td>
        <td class="text-end pe-4">
          <button class="btn btn-icon btn-edit me-1" onclick="openEdit('${k.maTK}')" title="Sửa"><i class="fas fa-pen"></i></button>
          <button class="btn btn-icon btn-delete" onclick="openDelete('${k.maTK}','${k.hoTen}')" title="Xóa"><i class="fas fa-trash"></i></button>
        </td>
      </tr>`).join('');

  const pages = Math.ceil(data.length / PAGE_SIZE);
  renderPagination('pagination', currentPage, pages, (p) => { currentPage = p; renderTable(data); });
}

function openAddKH() {
  ['akh-hoten','akh-sdt','akh-email','akh-pw'].forEach(id => document.getElementById(id).value = '');
  document.getElementById('add-kh-err').classList.add('d-none');
  new bootstrap.Modal(document.getElementById('addKHModal')).show();
}

async function saveKH() {
  const err  = document.getElementById('add-kh-err');
  const btn  = document.getElementById('btn-add-kh');
  const dto  = {
    hoTen:   document.getElementById('akh-hoten').value.trim(),
    sdt:     document.getElementById('akh-sdt').value.trim(),
    email:   document.getElementById('akh-email').value.trim(),
    matKhau: document.getElementById('akh-pw').value
  };
  err.classList.add('d-none');
  if (!dto.hoTen || !dto.sdt || !dto.matKhau) { err.textContent = 'Vui lòng điền đầy đủ!'; err.classList.remove('d-none'); return; }
  btn.disabled = true; btn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang lưu...';
  try {
    const res = await fetch(API + '/api/taikhoan/register', { method:'POST', headers:{'Content-Type':'application/json'}, body:JSON.stringify(dto) });
    if (!res.ok) { const m = await res.text(); throw new Error(m); }
    await loadKH();
    bootstrap.Modal.getInstance(document.getElementById('addKHModal')).hide();
    showToast('Thêm khách hàng thành công!', 'success');
  } catch(e) { err.textContent = 'Lỗi: ' + (e.message||'Thử lại!'); err.classList.remove('d-none'); }
  finally { btn.disabled = false; btn.innerHTML = '<i class="fas fa-save me-1"></i>Lưu'; }
}

function openEdit(maTK) {
  const kh = allKH.find(k => k.maTK === maTK);
  if (!kh) return;
  editMaTK = maTK;
  document.getElementById('ekh-ma').value    = kh.maTK;
  document.getElementById('ekh-hoten').value = kh.hoTen||'';
  document.getElementById('ekh-sdt').value   = kh.sdt||'';
  document.getElementById('ekh-email').value = kh.email||'';
  document.getElementById('ekh-diem').value  = kh.diemTichLuy||0;
  document.getElementById('ekh-hang').value  = kh.hangThanhVien||'Đồng';
  document.getElementById('ekh-pw').value    = '';
  document.getElementById('edit-kh-err').classList.add('d-none');
  new bootstrap.Modal(document.getElementById('editKHModal')).show();
}

async function updateKH() {
  const err = document.getElementById('edit-kh-err');
  const btn = document.getElementById('btn-edit-kh');
  const pw  = document.getElementById('ekh-pw').value;
  const dto = {
    hoTen:        document.getElementById('ekh-hoten').value.trim(),
    sdt:          document.getElementById('ekh-sdt').value.trim(),
    email:        document.getElementById('ekh-email').value.trim(),
    diemTichLuy:  parseInt(document.getElementById('ekh-diem').value)||0,
    hangThanhVien:document.getElementById('ekh-hang').value
  };
  if (pw) dto.matKhau = pw;
  err.classList.add('d-none');
  btn.disabled = true; btn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Đang lưu...';
  try {
    const res = await fetch(API + '/api/taikhoan/' + editMaTK, { method:'PUT', headers:{'Content-Type':'application/json'}, body:JSON.stringify(dto) });
    if (!res.ok) { const m = await res.text(); throw new Error(m); }
    await loadKH();
    bootstrap.Modal.getInstance(document.getElementById('editKHModal')).hide();
    showToast('Cập nhật thành công!', 'success');
  } catch(e) { err.textContent = 'Lỗi: ' + (e.message||'Thử lại!'); err.classList.remove('d-none'); }
  finally { btn.disabled = false; btn.innerHTML = '<i class="fas fa-save me-1"></i>Cập nhật'; }
}

function openDelete(maTK, hoTen) {
  deleteMaTK = maTK;
  document.getElementById('del-kh-name').textContent = hoTen;
  document.getElementById('btn-del-kh').onclick = confirmDeleteKH;
  new bootstrap.Modal(document.getElementById('deleteKHModal')).show();
}

async function confirmDeleteKH() {
  const btn = document.getElementById('btn-del-kh');
  btn.disabled = true; btn.textContent = 'Đang xóa...';
  try {
    const res = await fetch(API + '/api/taikhoan/' + deleteMaTK, { method:'DELETE' });
    if (!res.ok) {
      const raw = await res.text();
      let msg = raw;
      try { msg = JSON.parse(raw).message || raw; } catch {}
      throw new Error(msg);
    }
    allKH = allKH.filter(k => k.maTK !== deleteMaTK);
    applyFilter();
    bootstrap.Modal.getInstance(document.getElementById('deleteKHModal')).hide();
    showToast('Đã xóa khách hàng!', 'success');
  } catch(e) {
    bootstrap.Modal.getInstance(document.getElementById('deleteKHModal'))?.hide();
    showToast('⚠️ ' + (e.message||'Thử lại!'), 'danger');
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
