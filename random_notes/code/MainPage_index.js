import CSSModules from 'react-css-modules';
import { MainPage } from ./MainPage';
import styles from './MainPage.scss';
import { connect } from 'react-redux';

export default connect((Store) => {
    return {
        error: store.errorStore.error
    }
}) (CSSModules(MainPage, styles));

// and now for MainPage.js
...
import { isEmpty } from 'ramda';
import { receiveDrilldownQuery } from '../../app-redux/actions/drilldownQueryActions';

class MainPage extends Component {

    constructor(props) {
        super(props);
    }
    
    componentWillMoun() {
        if (this.props.location.query) {
            this.handlePotentialDrilldownQuery(this.props.location.query);
        }
    }
    
    handlePotentialDrilldownQuery(query) {
        if (!isEmpty(query)) {
            this.props.dispatch(receiveDrilldownQuery(query, this.props.location.search));
        }
    }

    render() {
        return (
            <div styleName="wrapper">
                <PageHeader />
                <div styleName="content">
                    <SearchWidge />
                    <QueryResultsWidget />
                </div>
                <PageFooter />
            </div>
        );
    }

}
