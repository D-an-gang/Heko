import { doc, getDoc, getFirestore } from "firebase/firestore";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import app from "../firebase";
import { Book, bookConverter } from "../models/book";
import styles from "./book_info.module.css"

const BookInfo = () => {
    const { id } = useParams()
    const [book, setBook] = useState<Book>(null)
    useEffect(() => {
        fetch(id)
    }, [])
    const fetch = async (id: string) => {
        const db = getFirestore(app)
        const snap = await getDoc(doc(db, "books", "" + id).withConverter(bookConverter))
        if (snap.exists()) {
            const res: Book = snap.data()
            setBook(res)
        } else {
            console.log("noooooooooo");
        }
    }
    if (!book) {
        return <>Loading...</>
    }
    return (<>
        <h1>{book.title}</h1>
        <div className={styles.info}>
            <img className={styles.cover} src={book.book_cover} />
            <div className={styles.infoes}>
                <p><span><strong>Author:</strong></span> {book.author}</p>
                <p><span><strong>Total chapters:</strong></span> {book.chapters_number}</p>
                <p><span><strong>Last update:</strong></span> {book.last_update.toDate().toDateString()}</p>
                <p>{book.genre.map((x: string) =>
                    <span className="badge rounded-pill bg-dark mx-1">{x}</span>)}</p>
                <strong>Description:</strong>
                <p>{book.description}</p>
            </div>
        </div>
    </>);
}


export default BookInfo;