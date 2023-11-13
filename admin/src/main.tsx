import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import ErrorPage from './error-page.tsx';
import App from './App.tsx';
import Users from './routes/users.tsx';

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "users",
        element: <><Users/></>
      },
      {
        path: "books",
        element: <></>
      },
      {
        path: "books/create",
        element: <></>
      },
      {
        path: "books/:id",
        element: <></>
      },
      {
        path: "chap/:id",
        element: <></>
      },
      {
        path: "chap/create",
        element: <></>
      },

    ]
  },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)
