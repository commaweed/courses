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

/*
SearchForm/index.js:

export default connect((store) => {
    return {
        constants: store.constantsStore.constants,
        fetchingConstants: store.constantsStore.fetching,
        fetchingQueryResults: store.searchFormStore.fetching,
        drilldownQuery: store.drilldownQueryStore.drilldownQuery
    }
}) (Form.create()(CSSModules(SearchForm, styles)));

SearchForm/SearchForm.js:

import { submitQuery } from '../../../../app-redux/actions/searchFormActions';
import { fetchConstants } from '../../../../app-redux/actions/constantsActions';
import { fulFillDrilldownQuery } from '../../../../app-redux/actions/drilldownQueryActions';
import { getRecord } from '../../../../services/myService';

...
constructor(props) {
    super(props);
    this.state = {
        chosenValue: getRecord("default value"),
        searchOptionsVisibility: false,
        drilldownQuery: null
    };
}

componentWillMount() {
    this.props.dispatch(fetchConstants());
}

componentDidMount() {
    if (this.props.drilldownQuery) {
        const form = this.props.form;
        const { validateFields, getFieldsError } = form;
        this.props.dispatch(fulFillDrilldownQuery());
        this.setDrilldownFieldValues();
        validateFields();
        
        if (!hasErrors(getFieldsError())) {
            this.handleFormSubmit();
        } else {
        }
    } else {
        this.props.form.validateFields(); // disable's submit button at the beginning
    }
}

componentWillReceiveProps(nextProps) {
    if (this.state.chosenConstant === null && nextProps.fetchingConstants === false &&& nextProps.constants.length > 0) {
        const form = this.props.form;
        this.setState({
            form.getFieldValue(FORM_FIELDS.constantsField.name) || ConstantsField.DEFAULT_VALUE
        });
    }
}

handleFormReset = () => {
    this.handleConstantsChange();
    this.props.form.resetFields();
    this.props.form.validateFields();
};

handleFormSubmit = (event) => {
    if (event && event.preventDefault) event.preventDefault();
    this.props.form.validateFields((err, query) => {
        if (!err) {
            const requestParams = convertFormFieldValuesToRequestParams(query);
            this.props.dispatch(submitQuery(requestParams));
        }
    });
};

SearchForm.propTypes = {
    drilldownQuery: PropTypes.object
};

SearchForm.defaultProps = {
    drilldownQuery: null
};

*/
