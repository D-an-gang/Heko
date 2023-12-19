import ReactDOM from 'react-dom/client'
import './index.css'
import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import ErrorPage from './error-page.tsx';
import App from './App.tsx';
import Users from './routes/users.tsx';
import BookInfo from './routes/book_info.tsx';

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: "users",
        element: <><Users /></>
      },
      {
        path: "books",
        element: <BookInfo></BookInfo>
      },
      {
        path: "books/create",
        element: <></>
      },
      {
        path: "books/:id",
        // TODO create book info page
        element: <BookInfo />
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
  <RouterProvider router={router} />
)
