import React, {Component} from "react";
import { Button, TextField } from '@material-ui/core';
import axios from 'axios';
import {Link} from "react-router-dom";
const axiosPOSTconfig = {headers: {'Content-Type': 'application/json'}};


class CreateReview extends Component{

    constructor(props) {
        super(props)
        this.state = {
            author: '',
            name: '',
            review: ''
        }
    }

    onChange = (event) => {
        this.setState({[event.target.id]: event.target.value});
    }

    onSubmit = (event) => {
        let {author, game, review} = this.state;
        event.preventDefault();
        axios.post('http://localhost:8082/reviews/create', JSON.stringify({
            'author': author,
            'game': game,
            'review': review
        }), axiosPOSTconfig)
            .then((response) => {
                this.setState({status: response.data.status});
            })
            .catch((error) => {console.log(error)});
    }

    render() {
        let {author, game, review} = this.state;
        return(
            <main>
                <div>
                    <form onSubmit={this.onSubmit}>
                        <TextField id="author" type="text" value={author} placeholder={"Author"} onChange={this.onChange}/><br/>
                        <TextField id="game" type="text" value={game} placeholder={"Game"} onChange={this.onChange}/><br/>
                        <TextField id="review" type="text" value={review} placeholder={"Review"} onChange={this.onChange}/><br/>
                        <br/>
                        <Button onClick={this.onSubmit} variant="contained" color="primary">Create Review</Button><br/>
                        <br/><Button component={Link} to="/Review" variant="contained" color="primary">Review's Table</Button>
                    </form>
                </div>
            </main>
        );
    }
}
export default CreateReview;
