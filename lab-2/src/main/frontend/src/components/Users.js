import React, {Component} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";

export class Users extends Component{
    constructor(props) {
        super(props);
        this.state = {
            rows: []
        };
    }

    componentDidMount() {
        axios.get('http://localhost:8082/users')
            .then((response) => {this.setState({rows: response.data.data});})
            .catch((error) => {console.log(error); this.setState({ message: error.message })});
    }

    render() {
        return (
            <main role="main" className="container">
                <div align="center">
                    {this.state.rows === null && <p>Loading menu...</p>}
                    <table>
                        <thead>
                        <tr><th>Id</th>|<th>Date</th>|<th>Email</th>|<th>Password</th>|<th>Username</th></tr>
                        </thead>
                        {this.state.rows && this.state.rows.map(user => (
                            <tbody>
                            <tr><td>{user.id}</td>|<td>{user.date}</td>|<td>{user.email}</td>|<td>{user.password}</td>|<td>{user.username}</td></tr>
                            </tbody>
                        ))
                        }
                    </table>
                    <div>
                        <Button component={Link} to="/CreateUser" variant="contained" color="primary">Add User</Button>
                        <Button component={Link} to="/Users" variant="contained" color="primary">Delete User</Button>

                    </div>
                </div>
            </main>
        );
    }
}
