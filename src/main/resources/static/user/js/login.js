
  const API_BASE_LOGIN = (typeof window !== 'undefined' && window.location && window.location.origin)
      ? window.location.origin
      : 'http://localhost:8080';

  // Nếu đã đăng nhập thì redirect thẳng
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
    if (!sdt || !pw) { showErr(err, 'Vui lòng nhập đầy đủ!'); return; }
    btn.disabled = true; btn.textContent = 'Đang đăng nhập...';
    try {
      const res = await fetch(API_BASE_LOGIN + '/api/taikhoan/login', {
        method: 'POST', headers: {'Content-Type':'application/json'},
        body: JSON.stringify({sdt, matKhau: pw})
      });
      if (!res.ok) { showErr(err, 'Sai số điện thoại hoặc mật khẩu!'); return; }
      const user = await res.json();
      setCurrentUser(user);
      const redirect = sessionStorage.getItem('arenax_redirect');
      sessionStorage.removeItem('arenax_redirect');
      if ((user.vaiTro || '').toUpperCase() === 'ADMIN') {
        window.location.href = '../admin/dashboard.html';
      } else {
        window.location.href = redirect || 'index.html';
      }
    } catch { showErr(err, 'Không kết nối được server!'); }
    finally { btn.disabled = false; btn.textContent = 'ĐĂNG NHẬP'; }
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
    if (!hoTen || !sdt || !pw) { showErr(err, 'Vui lòng điền đầy đủ!'); return; }
    if (pw !== pw2) { showErr(err, 'Mật khẩu nhập lại không khớp!'); return; }
    btn.disabled = true; btn.textContent = 'Đang đăng ký...';
    try {
      const res = await fetch(API_BASE_LOGIN + '/api/taikhoan/register', {
        method: 'POST', headers: {'Content-Type':'application/json'},
        body: JSON.stringify({hoTen, sdt, email, matKhau: pw})
      });
      if (!res.ok) {
        const msg = await res.text();
        showErr(err, msg || 'Đăng ký thất bại!');
        return;
      }
      succ.textContent = 'Đăng ký thành công! Chuyển sang đăng nhập...';
      succ.classList.remove('d-none');
      setTimeout(() => document.querySelector('[data-bs-target="#login"]').click(), 1500);
    } catch { showErr(err, 'Không kết nối được server!'); }
    finally { btn.disabled = false; btn.textContent = 'ĐĂNG KÝ TÀI KHOẢN'; }
  }

  function showErr(el, msg) { el.textContent = msg; el.classList.remove('d-none'); }

  document.addEventListener('keydown', e => {
    if (e.key === 'Enter') {
      if (document.getElementById('login-pw') === document.activeElement) doLogin();
      if (document.getElementById('reg-pw2') === document.activeElement) doRegister();
    }
  });

