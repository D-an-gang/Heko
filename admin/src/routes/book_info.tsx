import { useParams } from "react-router-dom";

const Book = () => {
    const {id} = useParams()
    return ( <>{id}</> );
}
 
export default Book;