package com.hung.demo_web.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hung.demo_web.dto.SanDto;
import com.hung.demo_web.entity.DonDatSan;
import com.hung.demo_web.entity.LoaiSan;
import com.hung.demo_web.entity.San;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.exception.LoiThaoTac;
import com.hung.demo_web.repository.DonDatSanRepository;
import com.hung.demo_web.repository.LoaiSanRepository;
import com.hung.demo_web.repository.SanRepository;
import com.hung.demo_web.service.SanService;

@Service
public class SanServiceimpl implements SanService {

    @Autowired private SanRepository sanRepository;
    @Autowired private LoaiSanRepository loaiSanRepository;
    @Autowired private DonDatSanRepository donDatSanRepository;

    private SanDto mapToDto(San entity) {
        SanDto dto = new SanDto();
        dto.setMaSan(entity.getMaSan());
        dto.setTenSan(entity.getTenSan());
        dto.setDiaChi(entity.getDiaChi());
        dto.setMoTa(entity.getMoTa());
        dto.setAnhChinh(entity.getAnhChinh());
        dto.setTienIch(entity.getTienIch());
        dto.setGiaThue(entity.getGiaThue());
        dto.setTrangThai(entity.getTrangThai());
        if (entity.getLoaiSan() != null) {
            dto.setMaLoaiSan(entity.getLoaiSan().getMaLoaiSan());
            dto.setTenLoaiSan(entity.getLoaiSan().getTenLoaiSan());
            dto.setSoNguoi(entity.getLoaiSan().getSoNguoi());
        }
        return dto;
    }

    @Override
    public List<SanDto> getAllSan() {
        return sanRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public SanDto getSanById(String maSan) {
        San entity = sanRepository.findById(maSan)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy sân: " + maSan));
        return mapToDto(entity);
    }

    @Override
    public SanDto createSan(SanDto sanDto) {
        San entity = new San();
        entity.setMaSan(sanDto.getMaSan() != null ? sanDto.getMaSan() : "S" + System.currentTimeMillis());
        entity.setTenSan(sanDto.getTenSan());
        entity.setDiaChi(sanDto.getDiaChi());
        entity.setMoTa(sanDto.getMoTa());
        entity.setTienIch(sanDto.getTienIch());
        entity.setAnhChinh(sanDto.getAnhChinh());
        entity.setGiaThue(sanDto.getGiaThue());
        entity.setTrangThai(sanDto.getTrangThai() != null ? sanDto.getTrangThai() : "EMPTY");
        if (sanDto.getMaLoaiSan() != null && !sanDto.getMaLoaiSan().isEmpty()) {
            LoaiSan loaiSan = loaiSanRepository.findById(sanDto.getMaLoaiSan())
                    .orElseThrow(() -> new KhongTimThay("Không tìm thấy loại sân"));
            entity.setLoaiSan(loaiSan);
        } else {
            throw new LoiThaoTac("Không được để trống Mã Loại Sân!");
        }
        return mapToDto(sanRepository.save(entity));
    }

    @Override
    public SanDto updateSan(String maSan, SanDto sanDto) {
        San entity = sanRepository.findById(maSan)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy sân: " + maSan));
        if (sanDto.getTenSan() != null) entity.setTenSan(sanDto.getTenSan());
        if (sanDto.getDiaChi() != null) entity.setDiaChi(sanDto.getDiaChi());
        if (sanDto.getMoTa() != null) entity.setMoTa(sanDto.getMoTa());
        if (sanDto.getTienIch() != null) entity.setTienIch(sanDto.getTienIch());
        if (sanDto.getAnhChinh() != null) entity.setAnhChinh(sanDto.getAnhChinh());
        if (sanDto.getGiaThue() != null) entity.setGiaThue(sanDto.getGiaThue());
        if (sanDto.getTrangThai() != null) entity.setTrangThai(sanDto.getTrangThai());
        if (sanDto.getMaLoaiSan() != null && !sanDto.getMaLoaiSan().isEmpty()) {
            LoaiSan loaiSan = loaiSanRepository.findById(sanDto.getMaLoaiSan())
                    .orElseThrow(() -> new KhongTimThay("Không tìm thấy loại sân"));
            entity.setLoaiSan(loaiSan);
        }
        return mapToDto(sanRepository.save(entity));
    }

    @Override
    @Transactional
    public void deleteSan(String maSan) {
        San entity = sanRepository.findById(maSan)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy sân: " + maSan));

        List<DonDatSan> donLienQuan = donDatSanRepository.findBySan_MaSan(maSan);

        long donChuaHuy = donLienQuan.stream()
                .filter(d -> !"Đã hủy".equalsIgnoreCase(d.getTrangThai()))
                .count();

        if (donChuaHuy > 0) {
            throw new LoiThaoTac(
                "Không thể xóa sân \"" + entity.getTenSan() + "\" vì đang có " + donChuaHuy +
                " đơn đặt chưa hủy. Hãy hủy toàn bộ đơn đặt của sân này trước."
            );
        }

        // Xóa các đơn đã hủy (tránh FK constraint)
        if (!donLienQuan.isEmpty()) {
            donDatSanRepository.deleteAll(donLienQuan);
        }

        sanRepository.delete(entity);
    }

    /**
     * Tìm sân còn trống theo ngày đá, khung giờ và loại sân.
     * Logic: Loại tất cả sân có đơn đặt chưa hủy bị trùng giờ trong ngày đó.
     */
    @Override
    public List<SanDto> timSanTrong(LocalDate ngayDa, LocalTime gioBatDau, LocalTime gioKetThuc, String maLoaiSan) {
        List<San> tatCaSan = (maLoaiSan != null && !maLoaiSan.isEmpty())
                ? sanRepository.findAll().stream()
                    .filter(s -> s.getLoaiSan() != null && s.getLoaiSan().getMaLoaiSan().equals(maLoaiSan))
                    .collect(Collectors.toList())
                : sanRepository.findAll();

        return tatCaSan.stream().filter(san -> {
            // Bỏ qua sân đang bảo trì
            if ("MAINTENANCE".equalsIgnoreCase(san.getTrangThai())) return false;
            // Kiểm tra xem sân có bị trùng giờ không
            List<DonDatSan> donCungNgay = donDatSanRepository.findBySan_MaSanAndNgayDa(san.getMaSan(), ngayDa);
            for (DonDatSan don : donCungNgay) {
                if (!"Đã hủy".equalsIgnoreCase(don.getTrangThai())) {
                    boolean trung = gioBatDau.isBefore(don.getGioKetThuc())
                            && gioKetThuc.isAfter(don.getGioBatDau());
                    if (trung) return false;
                }
            }
            return true;
        }).map(this::mapToDto).collect(Collectors.toList());
    }
}