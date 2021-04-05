import React, {Component} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";

export class Games extends Component{
    constructor(props) {
        super(props);
        this.state = {
            rows: []
        };
    }

    componentDidMount() {
        axios.get('http://localhost:8082/games')
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
                            <tr><th>id</th>|<th>date</th>|<th>description</th>|<th>name</th>|<th>price</th></tr>
                        </thead>
                        {this.state.rows && this.state.rows.map(game => (
                            <tbody>
                                <tr><td>{game.id}</td>|<td>{game.date}</td>|<td>{game.description}</td>|<td>{game.name}</td>|<td>{game.price}</td></tr>
                            </tbody>
                            ))
                        }
                    </table>
                    <div>
                        <Button component={Link} to="/CreateGame" variant="contained" color="primary">Add Game</Button>
                        <Button component={Link} to="/Games" variant="contained" color="primary">Delete Game</Button>

                    </div>
                </div>
            </main>
        );
    }
}
