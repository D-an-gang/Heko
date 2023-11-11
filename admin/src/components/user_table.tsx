import { User } from "../models/user"

const UserTable = ({ data }: { data: User[] }) => {
    console.table(data);
    return <>
        <table className="table table-hover">
            <thead>
                <tr>
                    <th scope="col">STT</th>
                    <th scope="col">Email</th>
                    <th scope="col">Username</th>
                    <th scope="col">Action</th>
                </tr>
            </thead>
            <tbody>{
                data.map((item: User) =>
                    <tr>
                        <td className="counterCell"></td>
                        <td>{item.email}</td>
                        <td>{item.username}</td>
                        <td><button type="button" className="btn btn-danger">Delete User</button></td>
                    </tr>
                )
            }
            </tbody>
        </table>
    </>

}
export default UserTable