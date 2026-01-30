package com.example.ch4_cloud_project_01.controller;

import com.example.ch4_cloud_project_01.dto.MemberRequest;
import com.example.ch4_cloud_project_01.dto.MemberResponse;
import com.example.ch4_cloud_project_01.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class MemberController {

    private final MemberService memberService;

    // 유저 등록
    @PostMapping("/members")
    public Long save(@RequestBody MemberRequest request) {
        log.info("[API - LOG] POST /api/members");
        return memberService.save(request);
    }

    // 유저 조회
    @GetMapping("/members/{id}")
    public MemberResponse find(@PathVariable Long id) {
        log.info("[API - LOG] GET /api/members/{}", id);
        return memberService.findById(id);
    }

    // 이미지 등록
    @PostMapping("/members/{id}/profile-image")
    public ResponseEntity<Void> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam MultipartFile file
    ) {
        memberService.uploadProfileImage(id, file);
        return ResponseEntity.ok().build();
    }

    // 이미지 조회
    @GetMapping("/members/{id}/profile-image")
    public ResponseEntity<String> getProfileImageUrl(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getProfileImageUrl(id));
    }
}

