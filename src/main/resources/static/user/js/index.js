
  document.getElementById('search-ngay').value = new Date().toISOString().split('T')[0];

  document.addEventListener('DOMContentLoaded', () => {
    if (!requireLogin()) return;
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
        ? '<p class="text-muted text-center py-4 col-12">ChÆ°a cÃ³ sÃ¢n nÃ o.</p>'
        : sans.slice(0,6).map(buildSanCard).join('');
    } catch {
      list.innerHTML = '<p class="text-danger text-center py-4 col-12"><i class="fas fa-exclamation-triangle me-2"></i>KhÃ´ng káº¿t ná»‘i Ä‘Æ°á»£c server backend!</p>';
    }
  }

  async function timSan() {
    const ngay = document.getElementById('search-ngay').value;
    const bd   = document.getElementById('search-gio-bd').value;
    const kt   = document.getElementById('search-gio-kt').value;
    const loai = document.getElementById('search-loai').value;
    if (!ngay || !bd || !kt) { alert('Vui lÃ²ng Ä‘iá»n Ä‘áº§y Ä‘á»§!'); return; }
    if (bd >= kt) { alert('Giá» báº¯t Ä‘áº§u pháº£i nhá» hÆ¡n giá» káº¿t thÃºc!'); return; }

    document.getElementById('search-results').style.display = 'block';
    document.getElementById('featured-section').style.display = 'none';
    document.getElementById('search-list').innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin fa-2x"></i><p>Äang tÃ¬m...</p></div>';
    document.getElementById('search-results').scrollIntoView({ behavior: 'smooth' });

    try {
      let url = API_BASE + '/api/san/tim-kiem?ngayDa=' + ngay + '&gioBatDau=' + bd + '&gioKetThuc=' + kt;
      if (loai) url += '&maLoaiSan=' + loai;
      const res  = await fetch(url);
      const sans = await res.json();
      document.getElementById('search-list').innerHTML = sans.length === 0
        ? '<p class="text-warning text-center py-4 col-12"><i class="fas fa-search me-2"></i>KhÃ´ng cÃ³ sÃ¢n trá»‘ng trong khung giá» nÃ y.</p>'
        : sans.map(buildSanCard).join('');
    } catch {
      document.getElementById('search-list').innerHTML = '<p class="text-danger text-center py-4 col-12">Lá»—i káº¿t ná»‘i server!</p>';
    }
  }

  function dongKQ() {
    document.getElementById('search-results').style.display = 'none';
    document.getElementById('featured-section').style.display = 'block';
  }

