
  document.getElementById('search-ngay').value = new Date().toISOString().split('T')[0];

  document.addEventListener('DOMContentLoaded', () => {
    const user = getCurrentUser();
    if (user) {
      const av = document.getElementById('nav-user-avatar');
      if (av) av.textContent = (user.hoTen || 'U')[0].toUpperCase();
    }
    loadLoaiSan(document.getElementById('search-loai'));
    loadFeatured();
  });

  async function loadFeatured() {
    const list = document.getElementById('featured-list');
    try {
      const res  = await fetch(API_BASE + '/api/san');
      if (!res.ok) throw new Error();
      const sans = await res.json();
      list.innerHTML = sans.length === 0
        ? '<p class="text-muted text-center py-4 col-12">Chưa có sân nào.</p>'
        : sans.slice(0,6).map(buildSanCard).join('');
    } catch {
      list.innerHTML = '<p class="text-danger text-center py-4 col-12"><i class="fas fa-exclamation-triangle me-2"></i>Không kết nối được server backend!</p>';
    }
  }

  async function timSan() {
    const ngay = document.getElementById('search-ngay').value;
    const bd   = document.getElementById('search-gio-bd').value;
    const kt   = document.getElementById('search-gio-kt').value;
    const loai = document.getElementById('search-loai').value;
    if (!ngay || !bd || !kt) { alert('Vui lòng điền đầy đủ!'); return; }
    if (bd >= kt) { alert('Giờ bắt đầu phải nhỏ hơn giờ kết thúc!'); return; }

    document.getElementById('search-results').style.display = 'block';
    document.getElementById('featured-section').style.display = 'none';
    document.getElementById('search-list').innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin fa-2x"></i><p>Đang tìm...</p></div>';
    document.getElementById('search-results').scrollIntoView({ behavior: 'smooth' });

    try {
      let url = API_BASE + '/api/san/tim-kiem?ngayDa=' + ngay + '&gioBatDau=' + bd + '&gioKetThuc=' + kt;
      if (loai) url += '&maLoaiSan=' + loai;
      const res  = await fetch(url);
      const sans = await res.json();
      document.getElementById('search-list').innerHTML = sans.length === 0
        ? '<p class="text-warning text-center py-4 col-12"><i class="fas fa-search me-2"></i>Không có sân trống trong khung giờ này.</p>'
        : sans.map(buildSanCard).join('');
    } catch {
      document.getElementById('search-list').innerHTML = '<p class="text-danger text-center py-4 col-12">Lỗi kết nối server!</p>';
    }
  }

  function dongKQ() {
    document.getElementById('search-results').style.display = 'none';
    document.getElementById('featured-section').style.display = 'block';
  }

