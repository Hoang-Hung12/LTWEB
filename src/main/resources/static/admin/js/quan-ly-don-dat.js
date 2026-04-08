document.addEventListener('DOMContentLoaded', async () => {
    await loadDons();
    await loadSansForCreate();
    // Set ngÃ y máº·c Ä‘á»‹nh lÃ  hÃ´m nay
    document.getElementById('c-ngay').value = new Date().toISOString().split('T')[0];
  });

  async function loadDons() {
    try {
      const res  = await fetch(API + '/api/dondatsan');
      allDons    = await res.json();
      allDons.sort((a,b) => new Date(b.ngayDat||0) - new Date(a.ngayDat||0));
      updateStatCards();
      applyFilter();
    } catch {
      document.getElementById('don-tbody').innerHTML =
        '<tr><td colspan="7" class="text-center text-danger py-4"><i class="fas fa-wifi-slash me-2"></i>Lá»—i káº¿t ná»‘i server!</td></tr>';
    }
  }

  function updateStatCards() {
    document.getElementById('cnt-cho').textContent  = allDons.filter(d => d.trangThai === 'Chá» xÃ¡c nháº­n thanh toÃ¡n').length;
    document.getElementById('cnt-xn').textContent   = allDons.filter(d => d.trangThai === 'ÄÃ£ xÃ¡c nháº­n').length;
    document.getElementById('cnt-ht').textContent   = allDons.filter(d => d.trangThai === 'ÄÃ£ hoÃ n thÃ nh').length;
    document.getElementById('cnt-huy').textContent  = allDons.filter(d => d.trangThai === 'ÄÃ£ há»§y').length;
  }

  function applyFilter() {
    const tt  = document.getElementById('filter-tt').value;
    const q   = (document.getElementById('search-don').value||'').toLowerCase();
    filtered  = allDons.filter(d => {
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
      total === 0 ? 'KhÃ´ng cÃ³ Ä‘Æ¡n nÃ o' : `Hiá»ƒn thá»‹ ${start+1}â€“${Math.min(start+data.length,total)} / ${total} Ä‘Æ¡n`;

    const tbody = document.getElementById('don-tbody');
    tbody.innerHTML = data.length === 0
      ? '<tr><td colspan="7" class="text-center text-muted py-4">KhÃ´ng cÃ³ Ä‘Æ¡n phÃ¹ há»£p.</td></tr>'
      : data.map(d => {
          const ttClass = statusClass(d.trangThai);
          const ngay   = d.ngayDa ? fmtDate(d.ngayDa) : 'â€”';
          const gio    = [(d.gioBatDau||'').substring(0,5),(d.gioKetThuc||'').substring(0,5)].join(' - ');
          const actions = buildActions(d);
          return `<tr>
            <td class="ps-4"><strong class="text-primary font-monospace" style="cursor:pointer" onclick="viewDon('${d.maDon}')">${d.maDon}</strong></td>
            <td>
              <div class="fw-bold">${d.tenKhachHang||'â€”'}</div>
              <div class="small text-muted">${d.sdtKhachHang||d.maKH||'â€”'}</div>
            </td>
            <td>
              <div>${d.tenSan||d.maSan||'â€”'}</div>
              <div class="small text-primary"><i class="fas fa-calendar me-1"></i>${ngay} &nbsp;<i class="fas fa-clock me-1"></i>${gio}</div>
            </td>
            <td class="text-danger fw-bold">${fmtMoney(d.tienSan)}</td>
            <td class="text-success fw-bold">${fmtMoney(d.tienCoc)}</td>
            <td><span class="status-badge ${ttClass}">${d.trangThai||'â€”'}</span></td>
            <td class="text-end pe-4">${actions}</td>
          </tr>`;
        }).join('');

    renderPagination(pages);
  }

  function buildActions(d) {
    const tt = (d.trangThai||'');
    let html = `<button class="btn btn-light btn-sm me-1" onclick="viewDon('${d.maDon}')" title="Chi tiáº¿t"><i class="fas fa-eye"></i></button>`;
    if (tt === 'Chá» xÃ¡c nháº­n thanh toÃ¡n') {
      html += `<button class="btn btn-info btn-sm me-1" onclick="xacNhanThanhToan('${d.maDon}')" title="XÃ¡c nháº­n thanh toÃ¡n"><i class="fas fa-money-check-dollar"></i> XÃ¡c nháº­n TT</button>`;
      html += `<button class="btn btn-outline-danger btn-sm" onclick="updateTT('${d.maDon}','ÄÃ£ há»§y')" title="Há»§y"><i class="fas fa-times"></i> Há»§y</button>`;
    } else if (tt === 'Chá» duyá»‡t') {
      html += `<button class="btn btn-success btn-sm me-1" onclick="updateTT('${d.maDon}','ÄÃ£ xÃ¡c nháº­n')" title="Duyá»‡t"><i class="fas fa-check"></i> Duyá»‡t</button>`;
      html += `<button class="btn btn-outline-danger btn-sm" onclick="updateTT('${d.maDon}','ÄÃ£ há»§y')" title="Há»§y"><i class="fas fa-times"></i> Há»§y</button>`;
    } else if (tt === 'ÄÃ£ xÃ¡c nháº­n') {
      html += `<button class="btn btn-primary btn-sm me-1" onclick="updateTT('${d.maDon}','ÄÃ£ hoÃ n thÃ nh')" title="HoÃ n thÃ nh"><i class="fas fa-flag-checkered"></i> Xong</button>`;
      html += `<button class="btn btn-outline-danger btn-sm" onclick="updateTT('${d.maDon}','ÄÃ£ há»§y')" title="Há»§y"><i class="fas fa-times"></i> Há»§y</button>`;
    }
    return html;
  }

  function statusClass(tt) {
    if (!tt) return 'status-cho';
    const t = tt.toLowerCase();
    if (t.includes('chá»'))  return 'status-cho';
    if (t.includes('há»§y'))  return 'status-huy';
    if (t.includes('hoÃ n')) return 'status-hoantat';
    if (t.includes('xÃ¡c'))  return 'status-xacnhan';
    return 'status-cho';
  }

  function renderPagination(pages) {
    const pag = document.getElementById('pagination');
    if (pages <= 1) { pag.innerHTML = ''; return; }
    let html = '';
    html += `<button class="btn btn-sm btn-outline-secondary" onclick="goPage(${currentPage-1})" ${currentPage===1?'disabled':''}><i class="fas fa-chevron-left"></i></button>`;
    for (let p = 1; p <= pages; p++) {
      if (pages > 7 && p > 2 && p < pages-1 && Math.abs(p-currentPage) > 1) {
        if (p===3||p===pages-2) html += `<button class="btn btn-sm btn-outline-secondary" disabled>â€¦</button>`;
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
    const confirm_msg = trangThai === 'ÄÃ£ há»§y'
      ? `XÃ¡c nháº­n Há»¦Y Ä‘Æ¡n ${maDon}?`
      : `XÃ¡c nháº­n cáº­p nháº­t Ä‘Æ¡n ${maDon} â†’ "${trangThai}"?`;
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
      showToast(`ÄÃ£ cáº­p nháº­t Ä‘Æ¡n ${maDon} â†’ ${trangThai}`, trangThai === 'ÄÃ£ há»§y' ? 'danger' : 'success');
      if (viewingDon && viewingDon.maDon === maDon) viewDon(maDon);
    } catch(e) {
      showToast('Lá»—i: ' + (e.message||'Vui lÃ²ng thá»­ láº¡i!'), 'danger');
    }
  }

  async function xacNhanThanhToan(maDon) {
    const ok = await askConfirm(`XÃ¡c nháº­n Ä‘Ã£ nháº­n chuyá»ƒn khoáº£n cho Ä‘Æ¡n ${maDon}?`);
    if (!ok) return;
    try {
      const admin = JSON.parse(localStorage.getItem('arenax_user') || '{}');
      const maAdmin = admin.maTK || '';
      const res = await fetch(
        `${API}/api/dondatsan/${encodeURIComponent(maDon)}/xac-nhan-thanh-toan?maAdmin=${encodeURIComponent(maAdmin)}`,
        { method: 'PATCH' }
      );
      if (!res.ok) throw new Error(await res.text());
      const updated = await res.json();
      const idx = allDons.findIndex(d => d.maDon === maDon);
      if (idx >= 0) allDons[idx] = updated;
      updateStatCards();
      applyFilter();
      showToast(`ÄÃ£ xÃ¡c nháº­n thanh toÃ¡n cho Ä‘Æ¡n ${maDon}`, 'success');
      if (viewingDon && viewingDon.maDon === maDon) viewDon(maDon);
    } catch (e) {
      showToast('Lá»—i xÃ¡c nháº­n thanh toÃ¡n: ' + (e.message || 'Vui lÃ²ng thá»­ láº¡i!'), 'danger');
    }
  }

  function viewDon(maDon) {
    const d = allDons.find(x => x.maDon === maDon);
    if (!d) return;
    viewingDon = d;
    document.getElementById('d-ma').textContent      = d.maDon;
    document.getElementById('d-kh').textContent      = (d.tenKhachHang||'â€”') + (d.sdtKhachHang ? ' Â· ' + d.sdtKhachHang : '');
    document.getElementById('d-san').textContent     = d.tenSan || d.maSan || 'â€”';
    document.getElementById('d-ngay').textContent    = d.ngayDa ? fmtDate(d.ngayDa) : 'â€”';
    document.getElementById('d-gio').textContent     = [(d.gioBatDau||'').substring(0,5),(d.gioKetThuc||'').substring(0,5)].join(' â€“ ');
    document.getElementById('d-tien').textContent    = fmtMoney(d.tienSan);
    document.getElementById('d-coc').textContent     = fmtMoney(d.tienCoc);
    document.getElementById('d-diem').textContent    = (d.diemThuong||0) + ' Ä‘iá»ƒm';
    document.getElementById('d-pttt').textContent    = d.phuongThucThanhToan === 'CHUYEN_KHOAN' ? 'Chuyá»ƒn khoáº£n' : 'Tiá»n máº·t (Ä‘Ã£ chuyá»ƒn cá»c)';
    document.getElementById('d-admin-tt').textContent = d.maAdminXacNhanThanhToan || 'â€”';
    document.getElementById('d-time-tt').textContent = d.thoiGianXacNhanThanhToan ? new Date(d.thoiGianXacNhanThanhToan).toLocaleString('vi-VN') : 'â€”';
    document.getElementById('d-tt').innerHTML        = `<span class="status-badge ${statusClass(d.trangThai)}">${d.trangThai||'â€”'}</span>`;
    document.getElementById('d-ngaydat').textContent = d.ngayDat ? new Date(d.ngayDat).toLocaleString('vi-VN') : 'â€”';
    const proofWrap = document.getElementById('d-proof-wrap');
    proofWrap.style.display = 'none';
    loadProofByDon(d.maDon);

    // Actions trong modal
    const actDiv = document.getElementById('d-actions');
    actDiv.innerHTML = '';
    const tt = d.trangThai || '';
    if (tt === 'Chá» xÃ¡c nháº­n thanh toÃ¡n') {
      actDiv.innerHTML = `
        <button class="btn btn-info flex-fill" onclick="xacNhanThanhToan('${d.maDon}');closeDetail()">
          <i class="fas fa-money-check-dollar me-1"></i>XÃ¡c nháº­n thanh toÃ¡n
        </button>
        <button class="btn btn-outline-danger flex-fill" onclick="updateTT('${d.maDon}','ÄÃ£ há»§y');closeDetail()">
          <i class="fas fa-times me-1"></i>Há»§y
        </button>`;
    } else if (tt === 'Chá» duyá»‡t') {
      actDiv.innerHTML = `
        <button class="btn btn-success flex-fill" onclick="updateTT('${d.maDon}','ÄÃ£ xÃ¡c nháº­n');closeDetail()">
          <i class="fas fa-check me-1"></i>Duyá»‡t
        </button>
        <button class="btn btn-outline-danger flex-fill" onclick="updateTT('${d.maDon}','ÄÃ£ há»§y');closeDetail()">
          <i class="fas fa-times me-1"></i>Há»§y
        </button>`;
    } else if (tt === 'ÄÃ£ xÃ¡c nháº­n') {
      actDiv.innerHTML = `
        <button class="btn btn-primary flex-fill" onclick="updateTT('${d.maDon}','ÄÃ£ hoÃ n thÃ nh');closeDetail()">
          <i class="fas fa-flag-checkered me-1"></i>HoÃ n thÃ nh
        </button>
        <button class="btn btn-outline-danger flex-fill" onclick="updateTT('${d.maDon}','ÄÃ£ há»§y');closeDetail()">
          <i class="fas fa-times me-1"></i>Há»§y
        </button>`;
    } else {
      actDiv.innerHTML = `<button class="btn btn-secondary flex-fill" onclick="closeDetail()">ÄÃ³ng</button>`;
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
      sel.innerHTML = '<option value="">-- Chá»n sÃ¢n --</option>';
      sans.filter(s => (s.trangThai||'').toUpperCase() !== 'MAINTENANCE')
          .forEach(s => {
            const opt = document.createElement('option');
            opt.value = s.maSan;
            opt.textContent = s.tenSan + ' â€“ ' + fmtMoney(s.giaThue) + '/1.5h';
            sel.appendChild(opt);
          });
    } catch {}
  }

  async function submitCreate() {
    const err  = document.getElementById('create-err');
    const btn  = document.getElementById('btn-create');
    const maKH = document.getElementById('c-makh').value.trim();
    const maSan= document.getElementById('c-san').value;
    const ngay = document.getElementById('c-ngay').value;
    const bd   = document.getElementById('c-bd').value;
    const kt   = document.getElementById('c-kt').value;
    const km   = document.getElementById('c-km').value.trim();
    err.classList.add('d-none');
    if (!maKH||!maSan||!ngay||!bd||!kt) {
      err.textContent = 'Vui lÃ²ng Ä‘iá»n Ä‘áº§y Ä‘á»§ thÃ´ng tin!';
      err.classList.remove('d-none');
      return;
    }
    if (bd >= kt) {
      err.textContent = 'Giá» báº¯t Ä‘áº§u pháº£i nhá» hÆ¡n giá» káº¿t thÃºc!';
      err.classList.remove('d-none');
      return;
    }
    const dto = {
      maKH, maSan, ngayDa: ngay, gioBatDau: bd, gioKetThuc: kt,
      phuongThucThanhToan: 'CHUYEN_KHOAN',
      daThanhToanCoc: true,
      chungTuThanhToan: 'ADMIN_CREATE'
    };
    if (km) dto.maCodeKhuyenMai = km;
    btn.disabled = true;
    btn.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Äang táº¡o...';
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
      showToast('Táº¡o Ä‘Æ¡n ' + newDon.maDon + ' thÃ nh cÃ´ng!', 'success');
    } catch(e) {
      err.textContent = 'Lá»—i: ' + (e.message||'Vui lÃ²ng thá»­ láº¡i!');
      err.classList.remove('d-none');
    } finally {
      btn.disabled = false;
      btn.innerHTML = '<i class="fas fa-save me-1"></i>Táº¡o Ä‘Æ¡n';
    }
  }

  // Helpers
  function fmtDate(d) {
    if (!d) return 'â€”';
    const p = String(d).split('-');
    return p.length === 3 ? `${p[2]}/${p[1]}/${p[0]}` : d;
  }
  function fmtMoney(n) {
    if (n == null) return 'â€”';
    return Number(n).toLocaleString('vi-VN') + 'Ä‘';
  }
  function showToast(msg, type='success') {
    const t = document.createElement('div');
    t.style.cssText = 'position:fixed;top:20px;right:20px;z-index:9999;min-width:280px';
    t.innerHTML = `<div class="alert alert-${type==='success'?'success':'danger'} shadow mb-0">${msg}</div>`;
    document.body.appendChild(t);
    setTimeout(() => t.remove(), 3000);
  }
  async function loadProofByDon(maDon) {
    const proofWrap = document.getElementById('d-proof-wrap');
    const imgEl = document.getElementById('d-proof');
    const linkEl = document.getElementById('d-proof-link');
    try {
      const res = await fetch(`${API}/api/dondatsan/${encodeURIComponent(maDon)}/chung-tu`);
      if (!res.ok) throw new Error();
      const data = await res.json();
      if (!data.chungTuThanhToan) {
        proofWrap.style.display = 'none';
        return;
      }
      imgEl.src = data.chungTuThanhToan;
      linkEl.href = data.chungTuThanhToan;
      proofWrap.style.display = 'block';
    } catch (_) {
      proofWrap.style.display = 'none';
    }
  }
  function askConfirm(msg) {
    return new Promise(resolve => {
      const modalEl = document.getElementById('confirmActionModal');
      const okBtn = document.getElementById('confirmActionOk');
      document.getElementById('confirmActionText').textContent = msg;
      const modal = new bootstrap.Modal(modalEl);
      const handleOk = () => { cleanup(); resolve(true); modal.hide(); };
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
</script>
</body>

