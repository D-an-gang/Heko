// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
export const firebaseConfig = {
  apiKey: "AIzaSyAtSGtnXLfL4Y5-1t5AYK4d_sPusczRmJg",
  authDomain: "heko-a6b88.firebaseapp.com",
  databaseURL: "https://heko-a6b88-default-rtdb.asia-southeast1.firebasedatabase.app",
  projectId: "heko-a6b88",
  storageBucket: "heko-a6b88.appspot.com",
  messagingSenderId: "607857336528",
  appId: "1:607857336528:web:9687ce0b3180c2d84cbed5"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export default app