package com.example.bmillion_backend.service;

import com.example.bmillion_backend.core.error.ErrorCode;
import com.example.bmillion_backend.core.error.exception.NotFoundException;
import com.example.bmillion_backend.core.error.exception.UnAuthorizedException;
import com.example.bmillion_backend.dto.PostListResponseDto;
import com.example.bmillion_backend.dto.PostRequestDto;
import com.example.bmillion_backend.dto.PostResponseDto;
import com.example.bmillion_backend.entity.PostEntity;
import com.example.bmillion_backend.entity.UserEntity;
import com.example.bmillion_backend.repo.PostRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private S3Service s3Service;

    public void createPost(PostRequestDto postRequest, MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        UserEntity user = userService.findUserByToken(request);
        if (user == null)
            throw new NotFoundException("로그인 후 게시글 작성이 가능합니다", ErrorCode.NOT_FOUND_EXCEPTION);

        PostEntity post = new PostEntity();
        post.setUser(user);
        post.setContent(postRequest.getContent());
        postRepo.save(post);
        if (multipartFile != null) {
            s3Service.uploadFile(post, multipartFile);
        }
    }

    public void updatePost(Long post_id, PostRequestDto postRequest, MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        UserEntity user = userService.findUserByToken(request);
        if (user == null)
            throw new UnAuthorizedException("게시글 수정 권한이 없습니다", ErrorCode.UNAUTHORIZED_EXCEPTION);

        PostEntity post = postRepo.findById(post_id).orElse(null);
        if (post == null)
            throw new NotFoundException("수정할 수 없는 게시글입니다", ErrorCode.NOT_FOUND_EXCEPTION);
        if (post.getUser() != user)
            throw new UnAuthorizedException("게시글 수정 권한이 없습니다", ErrorCode.UNAUTHORIZED_EXCEPTION);

        if (post.getFileName() != null) {
            s3Service.deleteFile(post.getFileName());
        }
        if (multipartFile != null) {
            s3Service.uploadFile(post, multipartFile);
        }
        post.setContent(postRequest.getContent());
    }

    public void deletePost(Long post_id, HttpServletRequest request) {
        UserEntity user = userService.findUserByToken(request);
        if (user == null)
            throw new UnAuthorizedException("게시글 삭제 권한이 없습니다", ErrorCode.UNAUTHORIZED_EXCEPTION);

        PostEntity post = postRepo.findById(post_id).orElse(null);
        if (post == null)
            throw new NotFoundException("삭제할 수 없는 게시글입니다", ErrorCode.NOT_FOUND_EXCEPTION);
        if (post.getUser() != user)
            throw new UnAuthorizedException("게시글 삭제 권한이 없습니다", ErrorCode.UNAUTHORIZED_EXCEPTION);

        if (post.getFileName() != null) {
            s3Service.deleteFile(post.getFileName());
        }
        postRepo.deleteById(post_id);
    }

    public List<PostListResponseDto> getPostList(HttpServletRequest request) {
        UserEntity user = userService.findUserByToken(request);
        if (user == null)
            throw new UnAuthorizedException("게시글 조회 권한이 없습니다", ErrorCode.UNAUTHORIZED_EXCEPTION);

        return postRepo.findByUserId(user.getId()).stream()
                .map(post -> {
                    PostListResponseDto response = new PostListResponseDto();
                    response.setId(post.getId());
                    response.setCreatedDate(post.getCreatedDate());
                    response.setContent(post.getContent());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public PostResponseDto getPost(Long post_id, HttpServletRequest request) {
        UserEntity user = userService.findUserByToken(request);
        if (user == null)
            throw new UnAuthorizedException("게시글 조회 권한이 없습니다", ErrorCode.UNAUTHORIZED_EXCEPTION);

        PostEntity post = postRepo.findById(post_id).orElse(null);
        if (post == null)
            throw new NotFoundException("조회할 수 없는 게시글입니다", ErrorCode.NOT_FOUND_EXCEPTION);
        if (post.getUser() != user)
            throw new UnAuthorizedException("게시글 조회 권한이 없습니다", ErrorCode.UNAUTHORIZED_EXCEPTION);

        PostResponseDto response = new PostResponseDto();
        response.setId(post.getId());
        response.setCreatedDate(post.getCreatedDate());
        response.setContent(post.getContent());
        response.setFileUrl(post.getFileUrl());
        return response;
    }

}
