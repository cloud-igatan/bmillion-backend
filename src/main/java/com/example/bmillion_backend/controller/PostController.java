package com.example.bmillion_backend.controller;

import com.example.bmillion_backend.dto.PostListResponseDto;
import com.example.bmillion_backend.dto.PostRequestDto;
import com.example.bmillion_backend.dto.PostResponseDto;
import com.example.bmillion_backend.repo.PostRepo;
import com.example.bmillion_backend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
@Tag(name = "Post Controller", description = "게시글 API")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepo postRepo;

    @Operation(summary = "게시글 작성")
    @PostMapping()
    public ResponseEntity<String> createPost(
            @RequestBody PostRequestDto postRequestDto,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            HttpServletRequest request) throws IOException {
        postService.createPost(postRequestDto, imageFile, request);
        return ResponseEntity.ok("게시글 작성 완료");
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{post_id}")
    public ResponseEntity<String> updatePost(
            @PathVariable Long post_id,
            @RequestBody PostRequestDto postRequestDto,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            HttpServletRequest request) throws IOException {
        postService.updatePost(post_id, postRequestDto, imageFile, request);
        return ResponseEntity.ok("게시글 수정 완료");
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable Long post_id, HttpServletRequest request) {
        postService.deletePost(post_id, request);
        return ResponseEntity.ok("게시글 삭제 완료");
    }

    @Operation(summary = "게시글 목록 조회")
    @GetMapping()
    public ResponseEntity<List<PostListResponseDto>> getPostList(HttpServletRequest request) {
        List<PostListResponseDto> postList = postService.getPostList(request);
        return ResponseEntity.ok(postList);
    }

    @Operation(summary = "게시글 조회")
    @GetMapping("/{post_id}")
    public ResponseEntity<PostResponseDto> getPost(@PathVariable Long post_id, HttpServletRequest request) {
        PostResponseDto post = postService.getPost(post_id, request);
        return ResponseEntity.ok(post);
    }

}
