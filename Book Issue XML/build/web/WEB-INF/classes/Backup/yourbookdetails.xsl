<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <xsl:param name="user"/>
        <head>
            <link rel="stylesheet" href="table.css"/>
        </head>
        <table class="table_your" cellpadding="20">
            <tr>
                <th>Unique ID</th>
                <th>Book Details</th>
            </tr>
            <xsl:foreach value="/issuebooks/books">
                <tr>
                    <td>
                        <b><xsl:value-of select="uid"/></b>
                   </td>
                   <td>
                       Department: <b><xsl:value-of select="dept"/></b><br></br>
                       Book: <b><xsl:value-of select="bookName"/></b><br></br>
                       Publisher: <xsl:value-of select="bookPub"/><br></br>
                       Return Date: <xsl:value-of select="day_r"/>-<xsl:value-of select="month_r"/>-<xsl:value-of select="year_r"/>
                   </td>
                </tr>
            </xsl:foreach>
        </table>
    </xsl:template>

</xsl:stylesheet>
