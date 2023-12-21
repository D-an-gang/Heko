import { Timestamp, addDoc, collection, query as qry, doc, getDocs, getFirestore, increment, updateDoc, where } from "firebase/firestore";
import { useNavigate, useSearchParams } from "react-router-dom";
import app from "../firebase";
import { Volume, volumeConverter } from "../models/volume";
import { useEffect, useState } from "react";
import ClassicEditor from "@ckeditor/ckeditor5-build-classic";
import Loading from "../components/loading";
import { CKEditor } from "@ckeditor/ckeditor5-react";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
interface volume {
    title: string
    cover: string
}
interface chapter {
    vol_id: string
    title: string
    content: string
}
const Chapter_create = () => {
    const [query] = useSearchParams()
    const navigate = useNavigate()
    const [vol, setVol] = useState<Volume[]>(null)
    const [content, setContent] = useState<chapter>(null)
    const [show, setShow] = useState(false);
    const [newVol, setNewVol] = useState<volume>({ title: "", cover: "" })
    const [triggerEffect, setTriggerEffect] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);
    const handleSave = async () => {
        console.log(newVol);
        const docRef = await addDoc(collection(db, "books", id, "volume"), {
            cover: newVol.cover,
            create_at: Timestamp.now(),
            title: newVol.title
        });
        await updateDoc(doc(db, "books", id, "volume", docRef.id), {
            id: docRef.id
        })
        handleClose()
        setTriggerEffect(true)
    }
    const id = query.get("book_id")
    const db = getFirestore(app)
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
        setTriggerEffect(false);
    }, [triggerEffect])
    const handleOnChange = (_, editor) => {
        setContent((prevValues) => ({ ...prevValues, content: editor.getData() }));
    }
    const saveVol = (event: React.ChangeEvent<HTMLSelectElement>) => {
        const volumn: string = event.target.value
        setContent((prevValues) => ({
            ...prevValues,
            vol_id: volumn
        }))
    }
    const saveChanges = async () => {
        console.table({
            vol_id: content.vol_id,
            content: content.content,
            create_at: Timestamp.now(),
            title: content.title
        });
        const docRef = await addDoc(collection(db, "books", id, "volume", content.vol_id, "chapters"), {
            content: content.content,
            create_at: Timestamp.now(),
            title: content.title
        });
        await updateDoc(doc(db, "books", id, "volume", content.vol_id, "chapters", docRef.id), {
            id: docRef.id
        })
        const q = qry(collection(getFirestore(app), "bookShelf"), where("book_id", "==", id))
        const snaps = await getDocs(q)
        snaps.forEach(async (doc) => {
            await updateDoc(doc.ref, {
                unread: increment(1)
            })
        })
        navigate("/books")
    }
    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = event.target;
        setNewVol((prevValues) => ({
            ...prevValues,
            [name]: value,
        }));
    };
    return <>{
        !vol ? (<Loading />) :
            <>
                <div className="chapter_create">
                    <h1>Create a new chapter</h1>
                    <div className="vol_group d-flex gap-1">
                        <button onClick={handleShow} className="btn btn-secondary">New Volume</button>
                        <select className="form-select" aria-label="Default select example" onChange={saveVol}>
                            {vol.map((item) =>
                                <option value={item.id} key={item.id}>{item.title}</option>
                            )}
                        </select>
                    </div>
                    <input onChange={(event) => setContent((prevValues) => ({ ...prevValues, title: event.target.value }))} type="text" className="form-control" placeholder="Default input" id="inputDefault"></input>
                    <CKEditor
                        editor={ClassicEditor}
                        data="😭"
                        config={{
                            toolbar: ['heading', '|', 'bold', 'italic', 'blockQuote'],
                            heading: {
                                options: [
                                    { model: 'paragraph', title: 'Paragraph', class: 'ck-heading_paragraph' },
                                    { model: 'heading1', view: 'h1', title: 'Heading 1', class: 'ck-heading_heading1' },
                                    { model: 'heading2', view: 'h2', title: 'Heading 2', class: 'ck-heading_heading2' },
                                    { model: 'heading3', view: 'h3', title: 'Heading 3', class: 'ck-heading_heading3' },
                                    { model: 'heading4', view: 'h4', title: 'Heading 4', class: 'ck-heading_heading4' },
                                    { model: 'heading5', view: 'h5', title: 'Heading 5', class: 'ck-heading_heading5' },
                                    { model: 'heading6', view: 'h6', title: 'Heading 6', class: 'ck-heading_heading6' }
                                ]
                            }
                        }}
                        onChange={handleOnChange}
                    />
                    <button className="btn btn-primary" onClick={saveChanges}>Save</button>
                </div>
                <Modal show={show} onHide={handleClose}>
                    <Modal.Header closeButton>
                        <Modal.Title>Modal heading</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <label htmlFor="vol_title">Volume Title</label>
                        <input onChange={handleInputChange} name="title" type="text" className="form-control" id="vol_title" />
                        <label htmlFor="vol_cover">Volume Cover</label>
                        <input onChange={handleInputChange} name="cover" type="text" className="form-control" id="vol_cover" />
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={handleClose}>
                            Close
                        </Button>
                        <Button variant="primary" onClick={handleSave}>
                            Save Changes
                        </Button>
                    </Modal.Footer>
                </Modal>
            </>
    }</>
}

export default Chapter_create;