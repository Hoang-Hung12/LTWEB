function getCurrentUser() {
  try { return JSON.parse(localStorage.getItem('arenax_user')); }
  catch (_) { return null; }
}

function requireAdmin() {
  const user = getCurrentUser();
  if (!user) {
    window.location.href = '../user/login.html';
    return null;
  }
  if ((user.vaiTro || '').toUpperCase() !== 'ADMIN') {
    window.location.href = '../user/index.html';
    return null;
  }
  return user;
}

function adminLogout() {
  try { localStorage.removeItem('arenax_user'); } catch (_) {}
  window.location.href = '../user/login.html';
}

document.addEventListener('DOMContentLoaded', () => {
  const admin = requireAdmin();
  if (!admin) return;
  document.querySelectorAll('#admin-name').forEach(el => {
    el.textContent = admin.hoTen || 'Admin';
  });
});
