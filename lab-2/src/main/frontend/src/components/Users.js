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
                        <tr><th width={50}>id</th><th width={100}>Date</th><th width={300}>Email</th><th width={300}>Password</th><th width={200}>Username</th></tr>
                        </thead>
                        {this.state.rows && this.state.rows.map(user => (
                            <tbody>
                            <tr><td width={50}>{user.id}</td><td width={100}>{user.date}</td><td width={300}>{user.email}</td><td width={300}>{user.password}</td><td width={200}>{user.username}</td></tr>
                            </tbody>
                        ))
                        }
                    </table>
                    <div>
                        <Button component={Link} to="/CreateUser" variant="contained" color="primary">Add User</Button>
                        <Button component={Link} to="/DeleteUser" variant="contained" color="primary">Delete User</Button>

                    </div>
                </div>
            </main>
        );
    }
}
