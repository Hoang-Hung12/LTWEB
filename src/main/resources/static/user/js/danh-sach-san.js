// Yêu cầu đăng nhập
document.addEventListener('DOMContentLoaded', () => { if (!requireLogin()) return; });


  const PAGE_SIZE = 6;
  let allSans    = [];
  let filtered   = [];
  let currentPage = 1;

  document.addEventListener('DOMContentLoaded', () => {
    loadLoaiSan(document.getElementById('filter-loai'));
    loadSans();
  });

  async function loadSans() {
    const list = document.getElementById('san-list');
    try {
      const res = await fetch(API_BASE + '/api/san');
      if (!res.ok) throw new Error();
      allSans = await res.json();
      applyFilters();
    } catch {
      list.innerHTML = '<p class="text-danger text-center py-4 col-12"><i class="fas fa-exclamation-triangle me-2"></i>Không kết nối được server!</p>';
    }
  }

  function applyFilters() {
    const loai = document.getElementById('filter-loai').value;
    const tt   = document.getElementById('filter-tt').value;
    const sort = document.getElementById('filter-sort').value;
    filtered   = [...allSans];
    if (loai) filtered = filtered.filter(s => s.maLoaiSan === loai);
    if (tt)   filtered = filtered.filter(s => (s.trangThai||'').toUpperCase() === tt.toUpperCase());
    if (sort === 'asc')  filtered.sort((a,b)=>(a.giaThue||0)-(b.giaThue||0));
    if (sort === 'desc') filtered.sort((a,b)=>(b.giaThue||0)-(a.giaThue||0));
    currentPage = 1;
    renderPage();
  }

  function renderPage() {
    const total  = filtered.length;
    const pages  = Math.ceil(total / PAGE_SIZE);
    const start  = (currentPage - 1) * PAGE_SIZE;
    const data   = filtered.slice(start, start + PAGE_SIZE);
    const list   = document.getElementById('san-list');
    const info   = document.getElementById('results-info');

    info.textContent = total === 0 ? '' : `Hiển thị ${start + 1}–${Math.min(start + data.length, total)} trong tổng số ${total} sân`;

    list.innerHTML = data.length === 0
      ? '<p class="text-muted text-center py-4 col-12"><i class="fas fa-search me-2"></i>Không có sân phù hợp.</p>'
      : data.map(buildSanCard).join('');

    renderPagination(pages);
  }

  function renderPagination(pages) {
    const wrap = document.getElementById('pagination-wrap');
    if (pages <= 1) { wrap.innerHTML = ''; return; }
    let html = '';
    html += `<button class="page-btn" onclick="goPage(${currentPage-1})" ${currentPage===1?'disabled':''}><i class="fas fa-chevron-left"></i></button>`;
    for (let p = 1; p <= pages; p++) {
      if (pages > 7 && p > 2 && p < pages - 1 && Math.abs(p - currentPage) > 1) {
        if (p === 3 || p === pages - 2) html += `<button class="page-btn" disabled>…</button>`;
        continue;
      }
      html += `<button class="page-btn ${p===currentPage?'active':''}" onclick="goPage(${p})">${p}</button>`;
    }
    html += `<button class="page-btn" onclick="goPage(${currentPage+1})" ${currentPage===pages?'disabled':''}><i class="fas fa-chevron-right"></i></button>`;
    wrap.innerHTML = html;
  }

  function goPage(p) {
    const pages = Math.ceil(filtered.length / PAGE_SIZE);
    if (p < 1 || p > pages) return;
    currentPage = p;
    renderPage();
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  async function xemChiTiet(maSan) {
    try {
      const res = await fetch(API_BASE + '/api/san/' + maSan);
      if (!res.ok) throw new Error();
      const san = await res.json();
      const img  = resolveSanImageUrl(san.anhChinh);
      const loai = san.tenLoaiSan || (san.soNguoi ? 'Sân ' + san.soNguoi + ' người' : 'Sân bóng');
      let tienIchHtml = '';
      if (san.tienIch) {
        san.tienIch.split(',').forEach(ti => {
          tienIchHtml += `<div class="amenity"><i class="fas fa-check-circle me-1 text-success"></i>${ti.trim()}</div>`;
        });
      }
      document.getElementById('modal-san-title').textContent  = san.tenSan;
      document.getElementById('modal-san-diachi').textContent = san.diaChi || '';
      document.getElementById('modal-san-loai').textContent   = loai;
      document.getElementById('modal-san-mota').textContent   = san.moTa || 'Chưa có mô tả.';
      document.getElementById('modal-san-tienich').innerHTML  = tienIchHtml || '<span class="text-muted">Chưa cập nhật</span>';
      document.getElementById('modal-san-gia').textContent    = formatCurrency(san.giaThue);
      document.getElementById('modal-san-img').src            = img;
      document.getElementById('modal-btn-dat').onclick        = () => { closeModal(); datSan(maSan); };
      document.getElementById('modal-btn-dat').disabled       = (san.trangThai||'').toUpperCase() === 'MAINTENANCE';
      document.getElementById('detailModal').style.display    = 'flex';
      document.body.style.overflow = 'hidden';
    } catch { alert('Không tải được thông tin sân!'); }
  }

  function closeModal() {
    const m = document.getElementById('detailModal');
    if (m) m.style.display = 'none';
    document.body.style.overflow = '';
  }

  document.addEventListener('keydown', e => { if (e.key === 'Escape') closeModal(); });

