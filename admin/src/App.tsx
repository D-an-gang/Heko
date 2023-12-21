import { ToastContainer } from 'react-toastify';
import './App.css'
import Root from './routes/root'
import { Outlet } from "react-router-dom";
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
    <>
      <Root />
      <div id="detail">
        <Outlet />
        <ToastContainer />
      </div>
    </>
  )
}

export default App
