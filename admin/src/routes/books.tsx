import { collection, getDocs, getFirestore } from "firebase/firestore";
import { Book, bookConverter } from "../models/book";
import { useState, useEffect } from 'react'
import Loading from "../components/loading";
import app from "../firebase";
import BookTable from "../components/book_table";

const Books = () => {
    const [list, setList] = useState<Book[]>([])
    const db = getFirestore(app);
    const [isLoading, setLoading] = useState(false)
    const fetch = async () => {
        setLoading(true)
        const querySnapshot = await getDocs(collection(db, "books").withConverter(bookConverter));
        const book_list: Book[] = []
        querySnapshot.forEach((doc) => {
            const book = doc.data()
            book_list.push(book)
        });
        setList(book_list)
        setLoading(false)
    }

    useEffect(() => { fetch() }, [])
    if (isLoading) {
        return <Loading />
    }
    return (<>
        <h1>Books</h1>
        <BookTable data={list} />
    </>);
}
export default Books;