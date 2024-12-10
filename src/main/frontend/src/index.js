import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import App from "./App";
import Login from "./Login"; // 로그인 페이지 컴포넌트
import Calender from "./Calender";
import Calender2 from "./Calender2";

import { ImageProvider } from "./ImageContext";
import ShowDiary from "./ShowDiary";
import ThirdPage from "./ThirdPage";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <Router>
      <ImageProvider>
        <Routes>
          <Route path="/" element={<App />} />
          <Route path="/login" element={<Login />} />
          <Route path="/ThirdPage" element={<ThirdPage />} />
          <Route path="/calender" element={<Calender />} />
          <Route path="/Calender2" element={<Calender2 />} />
          <Route path="/ShowDiary" element={<ShowDiary />} />
        </Routes>
      </ImageProvider>
    </Router>
  </React.StrictMode>,
  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
