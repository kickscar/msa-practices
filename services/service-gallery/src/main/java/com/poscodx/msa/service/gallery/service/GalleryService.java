package com.poscodx.msa.service.gallery.service;

import org.springframework.stereotype.Service;

import com.poscodx.msa.service.gallery.repository.GalleryRepository;
import com.poscodx.msa.service.gallery.vo.GalleryVo;

import java.util.List;

@Service
public class GalleryService {

	private final GalleryRepository galleryRepository;

	public GalleryService(GalleryRepository galleryRepository) {
		this.galleryRepository = galleryRepository;
	}

	public List<GalleryVo> getGalleryImages() {
		return galleryRepository.findAll();
	}
	
	public Boolean deleteGalleryImage(Long no) {
		return galleryRepository.delete(no);
	}
	
	public Boolean addGalleryImage(GalleryVo vo) {
		return galleryRepository.insert(vo);
	}
}
