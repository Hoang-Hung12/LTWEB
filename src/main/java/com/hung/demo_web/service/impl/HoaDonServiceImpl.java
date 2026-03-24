package com.hung.demo_web.service.impl;

import com.hung.demo_web.dto.HoaDonDto;
import com.hung.demo_web.entity.*;
import com.hung.demo_web.exception.LoiThaoTac;
import com.hung.demo_web.exception.KhongTimThay;
import com.hung.demo_web.repository.*;
import com.hung.demo_web.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private DonDatSanRepository donDatSanRepository;
    @Autowired private DonDatDichVuRepository donDatDichVuRepository;
    @Autowired private TaiKhoanRepository taiKhoanRepository;

    private HoaDonDto mapToDTO(HoaDonEntity entity) {
        HoaDonDto dto = new HoaDonDto();
        dto.setMaHoaDon(entity.getMaHoaDon());
        dto.setTongTienDichVu(entity.getTongTienDichVu());
        dto.setTongThanhToan(entity.getTongThanhToan());
        dto.setNgayLap(entity.getNgayLap());
        
        if (entity.getDonDatSan() != null) {
            dto.setMaDon(entity.getDonDatSan().getMaDon());
        }
        if (entity.getNguoiLap() != null) {
            dto.setMaQLLap(entity.getNguoiLap().getMaTK());
            dto.setTenQLLap(entity.getNguoiLap().getHoTen());
        }
        return dto;
    }

    @Override
    public HoaDonDto xuatHoaDon(String maDon, String maQLLap) {
        // 1. TÌM KIẾM ĐÚNG TÊN ENTITY 'DonDatSan' VÀ 'TaiKhoan'
        DonDatSan donDat = donDatSanRepository.findById(maDon)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Đơn đặt sân"));
        TaiKhoan admin = taiKhoanRepository.findById(maQLLap)
                .orElseThrow(() -> new KhongTimThay("Không tìm thấy Admin"));

        if (!donDat.getTrangThai().equals("Hoàn thành")) {
            throw new LoiThaoTac("Đơn đặt sân chưa hoàn thành, không thể xuất hóa đơn!");
        }

        // 2. TÍNH TỔNG TIỀN DỊCH VỤ VỚI ĐÚNG ENTITY 'DonDat_DichVu'
        List<DonDat_DichVu> listDichVu = donDatDichVuRepository.findById_maDon(maDon);
        double tongTienDV = 0;
        for (DonDat_DichVu dv : listDichVu) {
            tongTienDV += dv.getThanhTien();
        }

        double tongThanhToan = donDat.getTienSan() + tongTienDV - donDat.getTienCoc();
        if (tongThanhToan < 0) tongThanhToan = 0;

        // 3. KHỞI TẠO ĐÚNG ENTITY 'HoaDon'
        HoaDonEntity hoaDon = new HoaDonEntity();
        hoaDon.setMaHoaDon("HD" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        hoaDon.setDonDatSan(donDat);
        hoaDon.setNguoiLap(admin);
        hoaDon.setTongTienDichVu(tongTienDV);
        hoaDon.setTongThanhToan(tongThanhToan);

        return mapToDTO(hoaDonRepository.save(hoaDon));
    }
}