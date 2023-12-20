import { CSSProperties } from "react"
import { Book } from "../models/book"
import { Link } from "react-router-dom"

const BookTable = ({ data }: { data: Book[] }) => {
    return <>
        <table className="table table-hover" style={style}>
            <thead>
                <tr>
                    <th scope="col">STT</th>
                    <th scope="col">Title</th>
                    <th scope="col">Author</th>
                    <th scope="col">Genre</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>{
                data.map((item: Book) =>
                    <tr key={item.id}>
                        <td className="counterCell"></td>
                        <td className="fw-bold">{item.title}</td>
                        <td>{item.author}</td>
                        <td>{item.genre.map((x: string) =>
                            <span className="badge rounded-pill bg-dark mx-1" key={Math.random()}>{x}</span>)}
                        </td>
                        <td>
                            <button type="button" className="btn btn-danger mx-1">Delete</button>
                            <Link type="button" to={'/books/' + item.id} className="btn btn-danger">Edit</Link>
                        </td>
                    </tr>
                )
            }
            </tbody>
        </table>
    </>

}
export default BookTable

const style: CSSProperties = {
    textAlign: "left"
}