import './App.css'
import Root from './routes/root'
import { Outlet } from "react-router-dom";
function App() {

  return (
    <>
      <Root/>
      <div id="detail">
        <Outlet/>
      </div>
    </>
  )
}

export default App
