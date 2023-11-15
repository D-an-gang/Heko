import { collection, getDocs, getFirestore } from "firebase/firestore";
import { Book, bookConverter } from "../models/book";
import { useState, useEffect } from 'react'
import Loading from "../components/loading";
import app from "../firebase";
import BookTable from "../components/book_table";

const book_list: Book[] = []
const Books = () => {
    const db = getFirestore(app);
    const [isLoading, setLoading] = useState(false)
    const fetch = async () => {
        setLoading(true)
        const querySnapshot = await getDocs(collection(db, "books").withConverter(bookConverter));
        querySnapshot.forEach((doc) => {
            const book = doc.data()
            book_list.push(book)
        });
        setLoading(false)
    }

    useEffect(() => { fetch() },[])
    if (isLoading) {
        return <Loading />
    }
    return (<>
        {console.log(book_list)}
        <h1>Books</h1>
        <BookTable data={book_list}/>
    </>);
}
export default Books;