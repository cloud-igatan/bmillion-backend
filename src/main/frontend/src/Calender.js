import React, { useContext, useRef, useState } from "react";
import styled from "styled-components";

import TextInput from "./Textinput";
import { ImageContext } from "./ImageContext";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";

function Calender() {
  const location = useLocation();
  const navigate = useNavigate();
  const fileInputRef = useRef(null);
  const { imageSrc, setImageSrc } = useContext(ImageContext) || {};
  const [text, setText] = useState("");
  const [file, setFile] = useState(null);

  const handleAddImage = () => {
    fileInputRef.current.click();
  };

  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      const imageUrl = URL.createObjectURL(selectedFile);
      setImageSrc(imageUrl);
      setFile(selectedFile);
    }
  };

  const handleSave = async () => {
    if (!text || !file) {
      alert("텍스트와 이미지를 모두 입력해주세요.");
      return;
    }

    const formData = new FormData();
    formData.append("postRequestDto", JSON.stringify({ content: text }));
    formData.append("multipartFile", file);

    try {
      const token = localStorage.getItem("jwt");
      console.log("저장된 JWT 토큰:", token);

      // 수정 모드인지 확인
      if (location.state?.editMode) {
        // 수정 모드: PUT 요청
        await axios.put(
          `http://43.201.103.60:8080/post/${location.state.id}`, // 수정 API 엔드포인트
          formData,
          {
            headers: {
              Authorization: ` ${token}`,
            },
          }
        );
        alert("수정 성공!");
      } else {
        // 새 일기 생성: POST 요청
        await axios.post(
          "http://43.201.103.60:8080/post", // 생성 API 엔드포인트
          formData,
          {
            headers: {
              Authorization: ` ${token}`,
            },
          }
        );
        alert("저장 성공!");
      }

      // 상태 초기화
      setText("");
      setFile(null);
      setImageSrc(null);

      // Navigate to ThirdPage
      navigate("/ThirdPage");
    } catch (error) {
      console.error("저장 실패:", error.response?.data || error.message);
      alert("저장에 실패했습니다. 다시 시도해주세요.");
    }
  };
  return (
    <PageContainer>
      <ContentContainer>
        <Title>오늘의 일기</Title>

        {/* 텍스트 입력 */}
        <TextInput text={text} setText={setText} imageSrc={imageSrc} />

        <ButtonWrapper>
          {/* 이미지 첨부 */}
          <StyledButton onClick={handleAddImage}>사진 첨부하기</StyledButton>

          {/* 저장 버튼 */}
          <StyledButton onClick={handleSave}>저장</StyledButton>
        </ButtonWrapper>

        {/* 파일 입력 숨김 */}
        <Input
          type="file"
          ref={fileInputRef}
          style={{ display: "none" }}
          onChange={handleFileChange}
          accept="image/*"
        />
      </ContentContainer>
    </PageContainer>
  );
}

export default Calender;

const PageContainer = styled.div`
  width: 100%;
  min-height: 100vh; /* 스마트폰 화면에 꽉 차는 높이 */
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5; /* 연한 회색 배경 */
`;

const ContentContainer = styled.div`
  width: 90%;
  max-width: 400px; /* 스마트폰 카드 스타일 */
  padding: 20px;
  border-radius: 14px;
  background-color: #ffffff; /* 흰색 카드 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 부드러운 그림자 */
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  box-sizing: border-box;
`;

const Title = styled.h1`
  font-size: 24px;
  font-weight: bold;
  color: #333333;
  text-align: center;
  margin: 0;
  width: 100%;
`;

const Input = styled.input`
  width: 100%;
  height: 50px;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fafafa;
  box-sizing: border-box;
`;

const ButtonWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: space-between;
  gap: 10px;
`;

const StyledButton = styled.button`
  flex: 1;
  height: 50px;
  border: none;
  border-radius: 8px;
  background-color: #e0e0e0; /* 연한 회색 버튼 */
  color: #333333; /* 짙은 회색 텍스트 */
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #bdbdbd; /* hover 시 더 진한 회색 */
  }
  &:not(:last-child) {
    margin-right: 10px; /* 버튼 간격 */
  }
`;
