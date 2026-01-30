package com.example.ch4_cloud_project_01.service;

import com.example.ch4_cloud_project_01.dto.MemberRequest;
import com.example.ch4_cloud_project_01.dto.MemberResponse;
import com.example.ch4_cloud_project_01.entity.Member;
import com.example.ch4_cloud_project_01.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

    public Long save(MemberRequest request) {
        Member member = new Member(
                request.getName(),
                request.getAge(),
                request.getMbti()
        );
        return memberRepository.save(member).getId();
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti()
        );
    }

    @Transactional
    public void uploadProfileImage(Long memberId, MultipartFile file) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        String key = s3Service.upload(file);
        member.updateProfileImage(key);
    }

    public String getProfileImageUrl(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        if (member.getProfileImageKey() == null) {
            throw new IllegalStateException("프로필 이미지 없음");
        }

        return s3Service.getPresignedUrl(member.getProfileImageKey()).toString();
    }
}

