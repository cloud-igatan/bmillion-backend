import React from "react";
import styled from "styled-components";

function TextInput({ text, setText, imageSrc }) {
  return (
    <Container>
      {/* 텍스트 입력 영역 */}
      <TitleInput
        placeholder="일기 제목을 입력하세요"
        autoFocus
        value={text}
        onChange={(e) => setText(e.target.value)} // 텍스트 상태 업데이트
      />

      {/* 이미지가 있으면 이미지 박스에 표시 */}
      <ImageBox>
        {imageSrc ? (
          <StyledImage src={imageSrc} alt="이미지" />
        ) : (
          <PlaceholderText>이미지가 표시됩니다.</PlaceholderText>
        )}
      </ImageBox>
    </Container>
  );
}

export default TextInput;

// 스타일링 정의
const Container = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  align-items: center;
  justify-content: center;
  margin: 20px 0;
`;

const TitleInput = styled.textarea`
  width: 90%;
  height: 60px;
  padding: 10px;
  font-size: 1rem;
  border: 1px solid #ddd;
  border-radius: 12px;
  background-color: #fff;
  outline: none;
  resize: none;

  &:focus {
    border-color: #aaa;
  }
`;

const ImageBox = styled.div`
  width: 90%;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 2px dashed #ddd;
  border-radius: 12px;
  overflow: hidden;
  background-color: #f6f6f6;
`;

const StyledImage = styled.img`
  width: 100%;
  height: auto;
  object-fit: contain;
`;

const PlaceholderText = styled.p`
  font-size: 0.9rem;
  color: #aaa;
`;
