// Biến lưu tạm maTK khi tìm được qua SĐT (dùng cho bước 2 reset password)
let _forgotMaTK = null;

document.addEventListener('DOMContentLoaded', () => {
  // Nếu đã đăng nhập thì redirect
  const existing = getCurrentUser();
  if (existing) {
    window.location.href = (existing.vaiTro || '').toUpperCase() === 'ADMIN'
      ? '../admin/dashboard.html'
      : 'index.html';
  }

  // Nút "Quên mật khẩu?" → ẩn tabs, hiện panel quên mật khẩu
  document.getElementById('btn-show-forgot').addEventListener('click', e => {
    e.preventDefault();
    showForgotPanel();
  });

  // Nút "← Quay lại đăng nhập"
  document.getElementById('btn-back-login').addEventListener('click', e => {
    e.preventDefault();
    hideForgotPanel();
  });

  // Nút "← Nhập lại số điện thoại"
  document.getElementById('btn-back-step1').addEventListener('click', e => {
    e.preventDefault();
    showForgotStep(1);
  });
});

// ===================== ĐĂNG NHẬP =====================
async function doLogin() {
  const sdt = document.getElementById('login-sdt').value.trim();
  const pw  = document.getElementById('login-pw').value;
  const err = document.getElementById('login-error');
  const btn = document.getElementById('btn-login');
  if (!sdt || !pw) { showErr(err, 'Vui lòng nhập đầy đủ!'); return; }
  btn.disabled = true; btn.textContent = 'Đang đăng nhập...';
  try {
    const res = await fetch(API_BASE + '/api/taikhoan/login', {
      method: 'POST', headers: {'Content-Type':'application/json'},
      body: JSON.stringify({sdt, matKhau: pw})
    });
    if (!res.ok) {
      let msg = 'Sai số điện thoại hoặc mật khẩu!';
      if (res.status >= 500) msg = 'Lỗi server. Kiểm tra backend đã chạy và xem log console.';
      else {
        const t = await res.text();
        if (t && t.length < 200 && !t.trim().startsWith('<')) msg = t;
      }
      showErr(err, msg);
      return;
    }
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

// ===================== ĐĂNG KÝ =====================
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
    const res = await fetch(API_BASE + '/api/taikhoan/register', {
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

// ===================== QUÊN MẬT KHẨU =====================

/** Bước 1: Tra cứu SĐT */
async function doForgotStep1() {
  const sdt = document.getElementById('forgot-sdt').value.trim();
  const err = document.getElementById('forgot-error');
  const btn = document.getElementById('btn-forgot-next');
  err.classList.add('d-none');
  if (!sdt) { showErr(err, 'Vui lòng nhập số điện thoại!'); return; }

  btn.disabled = true; btn.textContent = 'Đang kiểm tra...';
  try {
    const res = await fetch(API_BASE + '/api/taikhoan/sdt/' + encodeURIComponent(sdt));
    if (!res.ok) {
      showErr(err, 'Số điện thoại chưa được đăng ký!');
      return;
    }
    const user = await res.json();
    _forgotMaTK = user.maTK; // lưu lại để dùng ở bước 2
    showForgotStep(2);
  } catch { showErr(err, 'Không kết nối được server!'); }
  finally { btn.disabled = false; btn.textContent = 'TIẾP THEO'; }
}

/** Bước 2: Đặt lại mật khẩu */
async function doResetPassword() {
  const pw   = document.getElementById('reset-pw').value;
  const pw2  = document.getElementById('reset-pw2').value;
  const err  = document.getElementById('reset-error');
  const succ = document.getElementById('reset-success');
  const btn  = document.getElementById('btn-reset');
  err.classList.add('d-none'); succ.classList.add('d-none');

  if (!pw || !pw2) { showErr(err, 'Vui lòng nhập mật khẩu mới!'); return; }
  if (pw !== pw2)  { showErr(err, 'Mật khẩu nhập lại không khớp!'); return; }
  if (pw.length < 6) { showErr(err, 'Mật khẩu phải có ít nhất 6 ký tự!'); return; }
  if (!_forgotMaTK) { showErr(err, 'Phiên làm việc hết hạn, hãy nhập lại SĐT!'); return; }

  btn.disabled = true; btn.textContent = 'Đang cập nhật...';
  try {
    const res = await fetch(API_BASE + '/api/taikhoan/' + encodeURIComponent(_forgotMaTK), {
      method: 'PUT',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify({matKhau: pw})
    });
    if (!res.ok) {
      const msg = await res.text();
      showErr(err, msg || 'Cập nhật mật khẩu thất bại!');
      return;
    }
    succ.textContent = 'Đổi mật khẩu thành công! Đang chuyển về đăng nhập...';
    succ.classList.remove('d-none');
    _forgotMaTK = null;
    setTimeout(() => hideForgotPanel(), 2000);
  } catch { showErr(err, 'Không kết nối được server!'); }
  finally { btn.disabled = false; btn.textContent = 'CẬP NHẬT MẬT KHẨU'; }
}

// ===================== HELPERS UI =====================
function showForgotPanel() {
  document.getElementById('panel-tabs').classList.add('d-none');
  document.getElementById('panel-forgot').classList.remove('d-none');
  showForgotStep(1);
}

function hideForgotPanel() {
  document.getElementById('panel-forgot').classList.add('d-none');
  document.getElementById('panel-tabs').classList.remove('d-none');
  _forgotMaTK = null;
  // reset fields
  document.getElementById('forgot-sdt').value = '';
  document.getElementById('reset-pw').value = '';
  document.getElementById('reset-pw2').value = '';
  document.getElementById('forgot-error').classList.add('d-none');
  document.getElementById('reset-error').classList.add('d-none');
  document.getElementById('reset-success').classList.add('d-none');
}

function showForgotStep(step) {
  document.getElementById('forgot-step1').classList.toggle('d-none', step !== 1);
  document.getElementById('forgot-step2').classList.toggle('d-none', step !== 2);
  if (step === 2) {
    document.getElementById('reset-pw').value = '';
    document.getElementById('reset-pw2').value = '';
    document.getElementById('reset-error').classList.add('d-none');
    document.getElementById('reset-success').classList.add('d-none');
  }
}

function showErr(el, msg) { el.textContent = msg; el.classList.remove('d-none'); }

document.addEventListener('keydown', e => {
  if (e.key === 'Enter') {
    const active = document.activeElement;
    if (active.id === 'login-pw') doLogin();
    if (active.id === 'reg-pw2') doRegister();
    if (active.id === 'forgot-sdt') doForgotStep1();
    if (active.id === 'reset-pw2') doResetPassword();
  }
});