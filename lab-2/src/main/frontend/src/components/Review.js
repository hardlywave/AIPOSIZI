import React, {Component} from "react";
import axios from "axios";
import {Link} from "react-router-dom";
import Button from "@material-ui/core/Button";

export class Review extends Component{
    constructor(props) {
        super(props);
        this.state = {
            rows: []
        };
    }

    componentDidMount() {
        axios.get('http://localhost:8082/reviews')
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
                        <tr><th>Id</th>|<th>Data</th>|<th>Reviews</th>|<th>Author</th>|<th>Game</th></tr>
                        </thead>
                        {this.state.rows && this.state.rows.map(reviews => (
                            <tbody>
                            <tr><td>{reviews.id}</td>|<td>{reviews.date}</td>|<td>{reviews.review}</td>|<td>{reviews.author.username}</td>|<td>{reviews.game.name}</td></tr>
                            </tbody>
                        ))
                        }
                    </table>
                    <div>
                        <Button component={Link} to="/CreateReview" variant="contained" color="primary">Add Review</Button>
                        <Button component={Link} to="/Review" variant="contained" color="primary">Delete Review</Button>

                    </div>
                </div>
            </main>
        );
    }
}
