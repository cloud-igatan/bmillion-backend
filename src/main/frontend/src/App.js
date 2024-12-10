import styled from "styled-components";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
function App() {
  const navigate = useNavigate();
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");

  const HandleSignUp = async () => {
    try {
      // 회원가입 요청
      const response = await axios.post(
        "http://43.201.103.60:8080/auth/register", // 실제 API 엔드포인트로 변경
        {
          userId,
          password,
        }
      );

      console.log("회원가입 성공:", response.data);
      alert("회원가입이 완료되었습니다!");

      // 로그인 페이지로 이동
      navigate("/login");
    } catch (error) {
      console.error("회원가입 실패:", error);
      alert("회원가입에 실패했습니다. 다시 시도해주세요.");
    }
  };
  return (
    <Wrapper>
      <Card>
        <Title>BMillion</Title>
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
        <Button onClick={HandleSignUp}>회원가입</Button>
      </Card>
    </Wrapper>
  );
}

export default App;

// 스타일 정의
const Wrapper = styled.div`
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f0f0; /* 연한 회색 배경 */
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
  background-color: #6c757d; /* 초록색 버튼 */
  color: white;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
  &:hover {
    background-color: #0056b3; /* hover 효과 */
  }
`;
