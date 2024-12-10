import styled from "styled-components";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
function Loggin() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");

  const HandleLogin = async () => {
    try {
      // 로그인 요청 보내기
      const response = await axios.post(
        "http://43.201.103.60:8080/auth/login", // 백엔드 로그인 API URL
        { userId, password },
        {
          headers: {
            "Content-Type": "application/json", // JSON 형식 요청
          },
        }
      );

      console.log("로그인 응답 데이터:", response.headers.authorization);

      // 서버가 JWT 토큰을 반환했다고 가정
      const token = response.headers.authorization;
      localStorage.setItem("jwt", token); // JWT 토큰을 로컬스토리지에 저장
      if (localStorage.getItem("jwt")) {
        alert("로그인 성공!");
        console.log("navigate 호출 준비"); // 디버깅 로그
        navigate("/ThirdPage"); // 인증 성공 시 이동
        console.log("navigate 호출 완료"); // 디버깅 로그
      } else {
        alert("로그인에 실패했습니다. 다시 시도해주세요.");
      }
    } catch (error) {
      console.error("로그인 실패 상태 코드:", error.response?.status);
      console.error("에러 메시지:", error.response?.data || error.message);
      alert(
        error.response?.data?.errorMessage ||
          "아이디 또는 비밀번호가 잘못되었습니다."
      );
    }
  };

  return (
    <Wrapper>
      <Card>
        <Title>로그인</Title>
        <InputContainer>
          <Input
            type="text"
            placeholder="아이디"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
          />
          <Input
            type="password"
            placeholder="패스워드"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </InputContainer>
        <Button onClick={HandleLogin}>로그인</Button>
      </Card>
    </Wrapper>
  );
}

export default Loggin;

// 스타일 정의
const Wrapper = styled.div`
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5; /* 연한 회색 배경 */
`;

const Card = styled.div`
  width: 90%;
  max-width: 400px; /* 스마트폰 카드 스타일 */
  padding: 20px;
  border-radius: 10px;
  background-color: #ffffff; /* 흰색 카드 */
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1); /* 그림자 효과 */
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Title = styled.h1`
  font-size: 24px;
  color: #333333;
  margin-bottom: 20px;
`;

const InputContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 15px; /* 입력 필드 간격 */
`;

const Input = styled.input`
  width: 100%;
  height: 50px;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #dddddd;
  border-radius: 8px;
  box-sizing: border-box;
  background-color: #fafafa;
`;

const Button = styled.button`
  width: 100%;
  height: 50px;
  margin-top: 20px;
  border: none;
  border-radius: 8px;
  background-color: #6c757d; /* 모던한 파란색 */
  color: white;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #0056b3; /* hover 효과 */
  }
`;
