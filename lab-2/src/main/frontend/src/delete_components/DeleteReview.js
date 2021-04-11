import React, {Component} from "react";
import { Button, TextField } from '@material-ui/core';
import axios from 'axios';
import {Link, withRouter} from "react-router-dom";

class DeleteReview extends Component{

    constructor(props) {
        super(props)
        this.state = {
            id: ""
        }
    }

    onChange = (event) => {
        this.setState({[event.target.id]: event.target.value});
    }

    onSubmit = (event) => {
        let{id} = this.state
        event.preventDefault();
        axios.delete(`http://localhost:8082/reviews/delete/${id}`)
            .then((response) => {
                this.setState({status: response.data.status});
            })
            .catch((error) => {console.log(error)});
    }

    render() {
        let {id} = this.state;
        return(
            <main>
                <div>
                    <form onSubmit={this.onSubmit}>
                        <TextField id="id" type="text" value={id} placeholder={"Enter id"} onChange={this.onChange}/><br/>
                        <br/>
                        <Button onClick={this.onSubmit} variant="contained" color="primary">Delete Review</Button>
                        <Button component={Link} to="/Reviews" variant="contained" color="primary" >Review's Table</Button>
                    </form>
                </div>
            </main>
        );
    }
}
export default withRouter(DeleteReview);
