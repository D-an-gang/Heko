import { Firestore, collection, getDocs, getFirestore } from "firebase/firestore";
import Chapter, { chapterConverter } from "../models/chapter";
import app from "../firebase";
import { useEffect, useState } from "react";
import Loading from "./loading";
import styles from "../routes/book_info.module.css"

const Chapter_list = ({ book_id, vol_id }: { book_id: string, vol_id: string }) => {
    const [chaps, setChaps] = useState<Chapter[]>(null)
    const fetch = async () => {
        const db: Firestore = getFirestore(app)
        const docs = await getDocs(collection(db, "books", book_id, "volume", vol_id, "chapters").withConverter(chapterConverter))
        if (!docs.empty) {
            const res: Chapter[] = []
            docs.forEach((doc) => {
                if (doc.exists) {
                    res.push(doc.data())
                }
            })
            setChaps(res)
        }
    }
    useEffect(() => {
        fetch()
    }, [])
    if (!chaps) {
        return <Loading />
    }
    return (<>
        {chaps.map((item) => <ul key={item.id}>
            <li><div className={styles.chap_item}>
                <strong>{item.title}</strong>
                <p>✏: {item.create_at.toDate().toDateString()}</p>
            </div></li>
        </ul>)}
    </>);
}

export default Chapter_list;