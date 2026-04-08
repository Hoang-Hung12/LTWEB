
  const API_BASE_LOGIN = 'http://localhost:8080';

  // Náº¿u Ä‘Ã£ Ä‘Äƒng nháº­p thÃ¬ redirect tháº³ng
  document.addEventListener('DOMContentLoaded', () => {
    const existing = getCurrentUser();
    if (existing) {
      window.location.href = (existing.vaiTro || '').toUpperCase() === 'ADMIN'
        ? '../admin/dashboard.html'
        : 'index.html';
    }
  });

  async function doLogin() {
    const sdt = document.getElementById('login-sdt').value.trim();
    const pw  = document.getElementById('login-pw').value;
    const err = document.getElementById('login-error');
    const btn = document.getElementById('btn-login');
    if (!sdt || !pw) { showErr(err, 'Vui lÃ²ng nháº­p Ä‘áº§y Ä‘á»§!'); return; }
    btn.disabled = true; btn.textContent = 'Äang Ä‘Äƒng nháº­p...';
    try {
      const res = await fetch(API_BASE_LOGIN + '/api/taikhoan/login', {
        method: 'POST', headers: {'Content-Type':'application/json'},
        body: JSON.stringify({sdt, matKhau: pw})
      });
      if (!res.ok) { showErr(err, 'Sai sá»‘ Ä‘iá»‡n thoáº¡i hoáº·c máº­t kháº©u!'); return; }
      const user = await res.json();
      setCurrentUser(user);
      const redirect = sessionStorage.getItem('arenax_redirect');
      sessionStorage.removeItem('arenax_redirect');
      if ((user.vaiTro || '').toUpperCase() === 'ADMIN') {
        window.location.href = '../admin/dashboard.html';
      } else {
        window.location.href = redirect || 'index.html';
      }
    } catch { showErr(err, 'KhÃ´ng káº¿t ná»‘i Ä‘Æ°á»£c server!'); }
    finally { btn.disabled = false; btn.textContent = 'ÄÄ‚NG NHáº¬P'; }
  }

  async function doRegister() {
    const hoTen  = document.getElementById('reg-hoten').value.trim();
    const sdt    = document.getElementById('reg-sdt').value.trim();
    const email  = document.getElementById('reg-email').value.trim();
    const pw     = document.getElementById('reg-pw').value;
    const pw2    = document.getElementById('reg-pw2').value;
    const err    = document.getElementById('reg-error');
    const succ   = document.getElementById('reg-success');
    const btn    = document.getElementById('btn-reg');
    err.classList.add('d-none'); succ.classList.add('d-none');
    if (!hoTen || !sdt || !pw) { showErr(err, 'Vui lÃ²ng Ä‘iá»n Ä‘áº§y Ä‘á»§!'); return; }
    if (pw !== pw2) { showErr(err, 'Máº­t kháº©u nháº­p láº¡i khÃ´ng khá»›p!'); return; }
    btn.disabled = true; btn.textContent = 'Äang Ä‘Äƒng kÃ½...';
    try {
      const res = await fetch(API_BASE_LOGIN + '/api/taikhoan/register', {
        method: 'POST', headers: {'Content-Type':'application/json'},
        body: JSON.stringify({hoTen, sdt, email, matKhau: pw})
      });
      if (!res.ok) {
        const msg = await res.text();
        showErr(err, msg || 'ÄÄƒng kÃ½ tháº¥t báº¡i!');
        return;
      }
      succ.textContent = 'ÄÄƒng kÃ½ thÃ nh cÃ´ng! Chuyá»ƒn sang Ä‘Äƒng nháº­p...';
      succ.classList.remove('d-none');
      setTimeout(() => document.querySelector('[data-bs-target="#login"]').click(), 1500);
    } catch { showErr(err, 'KhÃ´ng káº¿t ná»‘i Ä‘Æ°á»£c server!'); }
    finally { btn.disabled = false; btn.textContent = 'ÄÄ‚NG KÃ TÃ€I KHOáº¢N'; }
  }

  function showErr(el, msg) { el.textContent = msg; el.classList.remove('d-none'); }

  document.addEventListener('keydown', e => {
    if (e.key === 'Enter') {
      if (document.getElementById('login-pw') === document.activeElement) doLogin();
      if (document.getElementById('reg-pw2') === document.activeElement) doRegister();
    }
  });

