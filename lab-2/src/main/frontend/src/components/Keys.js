import React, {Component} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";

export class Keys extends Component{
    constructor(props) {
        super(props);
        this.state = {
            rows: []
        };
    }

    componentDidMount() {
        axios.get('http://localhost:8082/keys')
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
                        <tr><th>Id</th>|<th>Key</th>|<th>Game</th></tr>
                        </thead>
                        {this.state.rows && this.state.rows.map(key => (
                            <tbody>
                            <tr><td>{key.id}</td>|<td>{key.key}</td>|<td>{key.game.name}</td></tr>
                            </tbody>
                        ))
                        }
                    </table>
                    <div>
                        <Button component={Link} to="/CreateKey" variant="contained" color="primary">Add Key</Button>
                        <Button component={Link} to="/Keys" variant="contained" color="primary">Delete Key</Button>

                    </div>
                </div>
            </main>
        );
    }
}
