import { collection, getDocs, getFirestore, increment, query, updateDoc, where } from "firebase/firestore"
import app from "../firebase"
import { useEffect, useState } from "react"
import { Volume, volumeConverter } from "../models/volume"
import styles from "../routes/book_info.module.css"
import Chapter_list from "./chapter_list"
import { Link } from "react-router-dom"
import { Button } from "react-bootstrap"
import { toast } from "react-toastify"


const Volume_list = ({ id }: { id: string }) => {
    const [vol, setVol] = useState<Volume[]>(null)
    const fetch = async () => {
        const db = getFirestore(app)
        const docs = await getDocs(collection(db, "books", id, "volume").withConverter(volumeConverter))
        if (!docs.empty) {
            const res: Volume[] = []
            docs.forEach((item) => {
                res.push(item.data())
            })
            setVol(res)
        }
    }
    useEffect(() => {
        fetch()
    }, [])
    if (!vol) {
        return <div className="">loading....</div>
    }
    const handleClick = async () => {
        const q = query(collection(getFirestore(app), "bookShelf"), where("book_id", "==", id))
        const snaps = await getDocs(q)
        snaps.forEach(async (doc) => {
            await updateDoc(doc.ref, {
                unread: increment(1)
            })
        })
        toast.success('🦄 Notified! 🥳', {
            position: "bottom-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: "light",
        });
    }
    return <>
        <Link className="btn btn-primary my-3" to={"/chap/create?book_id=" + id}>Add a chapter</Link>
        <Button className="btn btn-secondary my-3 mx-2" onClick={handleClick}>Notifies users</Button>
        {vol.map((item) =>
            <div className={styles.vols} key={item.id}>
                <h3 className="fw-bold px-1">{item.title}</h3>
                <div className={styles.display_row}>
                    <img src={item.cover} alt="" />
                    <div className={styles.chap_list}>
                        <Chapter_list book_id={id} vol_id={item.id} />
                    </div>
                </div>
            </div>
        )}
    </>
}
export default Volume_list