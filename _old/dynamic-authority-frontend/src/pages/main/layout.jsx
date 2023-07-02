import {Layout} from "antd";
import AppHeader from "@/pages/main/AppHeader";
import AppFooter from "@/pages/main/AppFooter";
import AppNav from "@/pages/main/AppNav";

const {Header, Footer, Sider, Content} = Layout;

export default function MainLayout({children}) {
    return (
        <Layout style={{height: "100vh"}}>
            <Header><AppHeader/></Header>
            <Layout hasSider>
                <Sider style={{backgroundColor: "white", overflow: "auto"}}><AppNav/></Sider>
                <Content style={{backgroundColor: "white", overflow: "auto"}}>
                    <div style={{margin: "10px"}}>
                        {children}
                    </div>
                </Content>
            </Layout>
            <Footer><AppFooter/></Footer>
        </Layout>
    )
}