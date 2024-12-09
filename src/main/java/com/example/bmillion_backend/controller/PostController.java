package com.example.bmillion_backend.controller;

import com.example.bmillion_backend.dto.PostListResponseDto;
import com.example.bmillion_backend.dto.PostRequestDto;
import com.example.bmillion_backend.dto.PostResponseDto;
import com.example.bmillion_backend.repo.PostRepo;
import com.example.bmillion_backend.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> createPost(

            @Parameter(description = "게시글 글", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @RequestPart(required = false, value = "postRequestDto") PostRequestDto postRequestDto,

            @Parameter(description = "게시글 사진", content =
            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(required = false, value = "multipartFile") MultipartFile multipartFile,

            HttpServletRequest request) throws IOException {

        postService.createPost(postRequestDto, multipartFile, request);
        return ResponseEntity.ok("게시글 작성 완료");

    }

    @Operation(summary = "게시글 수정")
    @PutMapping(value = "/{post_id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> updatePost(
            @PathVariable(value = "post_id") Long post_id,

            @Parameter(description = "게시글 글", content =
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
            @RequestPart(required = false, value = "postRequestDto") PostRequestDto postRequestDto,

            @Parameter(description = "게시글 사진", content =
            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestPart(required = false, value = "multipartFile") MultipartFile multipartFile,

            HttpServletRequest request) throws IOException {

        postService.updatePost(post_id, postRequestDto, multipartFile, request);
        return ResponseEntity.ok("게시글 수정 완료");

    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable(value = "post_id") Long post_id, HttpServletRequest request) {
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
    public ResponseEntity<PostResponseDto> getPost(@PathVariable(value = "post_id") Long post_id, HttpServletRequest request) {
        PostResponseDto post = postService.getPost(post_id, request);
        return ResponseEntity.ok(post);
    }

}
