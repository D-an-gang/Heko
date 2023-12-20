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
import Books from './routes/books.tsx';
import Chapter_create from './routes/chap_create.tsx';

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
        element: <Books />
      },
      {
        path: "books/create",
        element: <></>
      },
      {
        path: "books/:id",
        element: <BookInfo />
      },
      {
        path: "chap/:id",
        element: <></>
      },
      {
        path: "chap/create",
        element: <Chapter_create />
      },

    ]
  },
]);

ReactDOM.createRoot(document.getElementById('root')!).render(
  <RouterProvider router={router} />
)
