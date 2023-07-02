import {Button, Col, Layout, Row} from "antd";
import MainFooter from "@/pages/main/component/MainFooter";
import MainHeader from "@/pages/main/component/MainHeader";
import MainSider from "@/pages/main/component/MainSider";

const {Header, Footer, Sider, Content} = Layout;

export default function MainLayout({children}) {
    return (
        <>
            <Layout style={{height: "100vh"}}>
                <Header>
                    <MainHeader/>
                </Header>
                <Layout hasSider>
                    <Sider style={{backgroundColor: "white", overflow: "auto"}}>
                        <MainSider/>
                    </Sider>
                    <Content style={{backgroundColor: "white", overflow: "auto"}}>
                        <div style={{margin: "10px"}}>
                            {children}
                        </div>
                    </Content>
                </Layout>
                <Footer>
                    <MainFooter/>
                </Footer>
            </Layout>
        </>
    )
}