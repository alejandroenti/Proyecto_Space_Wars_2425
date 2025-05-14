<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"/>
	<xsl:template match="/">
		<section id="battle-report">
			<h3>BATTLE STATISTICS</h3>
			<xsl:apply-templates />				
		</section>
	</xsl:template>
	
	<xsl:template match="battle_summary">
		<table>
			<thead>
				<tr>
					<th>Army Planet</th>
					<th>Unit</th>
					<th>Drops</th>
					<th>Initial Army Enemy</th>
					<th>Units</th>
					<th>Drops</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td><xsl:value-of select="planet/troops/troop[1]/name" /></td>
					<td><xsl:value-of select="planet/troops/troop[1]/units" /></td>
					<td><xsl:value-of select="planet/troops/troop[1]/drops" /></td>
					<td><xsl:value-of select="enemy/troops/troop[1]/name" /></td>
					<td><xsl:value-of select="enemy/troops/troop[1]/units" /></td>
					<td><xsl:value-of select="enemy/troops/troop[1]/drops" /></td>
				</tr>
				<tr>
					<td><xsl:value-of select="planet/troops/troop[2]/name" /></td>
					<td><xsl:value-of select="planet/troops/troop[2]/units" /></td>
					<td><xsl:value-of select="planet/troops/troop[2]/drops" /></td>
					<td><xsl:value-of select="enemy/troops/troop[2]/name" /></td>
					<td><xsl:value-of select="enemy/troops/troop[2]/units" /></td>
					<td><xsl:value-of select="enemy/troops/troop[2]/drops" /></td>
				</tr>
				<tr>
					<td><xsl:value-of select="planet/troops/troop[3]/name" /></td>
					<td><xsl:value-of select="planet/troops/troop[3]/units" /></td>
					<td><xsl:value-of select="planet/troops/troop[3]/drops" /></td>
					<td><xsl:value-of select="enemy/troops/troop[3]/name" /></td>
					<td><xsl:value-of select="enemy/troops/troop[3]/units" /></td>
					<td><xsl:value-of select="enemy/troops/troop[3]/drops" /></td>
				</tr>
				<tr>
					<td><xsl:value-of select="planet/troops/troop[4]/name" /></td>
					<td><xsl:value-of select="planet/troops/troop[4]/units" /></td>
					<td><xsl:value-of select="planet/troops/troop[4]/drops" /></td>
					<td><xsl:value-of select="enemy/troops/troop[4]/name" /></td>
					<td><xsl:value-of select="enemy/troops/troop[4]/units" /></td>
					<td><xsl:value-of select="enemy/troops/troop[4]/drops" /></td>
				</tr>
				<tr>
					<td><xsl:value-of select="planet/troops/troop[5]/name" /></td>
					<td><xsl:value-of select="planet/troops/troop[5]/units" /></td>
					<td><xsl:value-of select="planet/troops/troop[5]/drops" /></td>
				</tr>
				<tr>
					<td><xsl:value-of select="planet/troops/troop[6]/name" /></td>
					<td><xsl:value-of select="planet/troops/troop[6]/units" /></td>
					<td><xsl:value-of select="planet/troops/troop[6]/drops" /></td>
				</tr>
				<tr>
					<td><xsl:value-of select="planet/troops/troop[7]/name" /></td>
					<td><xsl:value-of select="planet/troops/troop[7]/units" /></td>
					<td><xsl:value-of select="planet/troops/troop[7]/drops" /></td>
				</tr>
			</tbody>
		</table>
		
		<table>
			<thead>
				<tr>
					<th colspan="2">Cost Army Planet</th>
					<th colspan="2">Cost Army Enemy</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Metal:</td>
					<td><xsl:value-of select="planet/cost/metal" /></td>
					<td>Metal:</td>
					<td><xsl:value-of select="enemy/cost/metal" /></td>
				</tr>
					<tr>
					<td>Deuterium:</td>
					<td><xsl:value-of select="planet/cost/deuterium" /></td>
					<td>Deuterium:</td>
					<td><xsl:value-of select="enemy/cost/deuterium" /></td>
				</tr>

			</tbody>
		</table>
		
		<table>
			<thead>
				<tr>
					<th colspan="2">Losses Army Planet</th>
					<th colspan="2">Losses Army Enemy</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Metal:</td>
					<td><xsl:value-of select="planet/losses/metal" /></td>
					<td>Metal:</td>
					<td><xsl:value-of select="enemy/losses/metal" /></td>
				</tr>
				<tr>
					<td>Deuterium:</td>
					<td><xsl:value-of select="planet/losses/deuterium" /></td>
					<td>Deuterium:</td>
					<td><xsl:value-of select="enemy/losses/deuterium" /></td>
				</tr>
				
					<tr>
					<td>Weighed:</td>
					<td><xsl:value-of select="planet/losses/weighed" /></td>
					<td>Weighed:</td>
					<td><xsl:value-of select="enemy/losses/weighed" /></td>
				</tr>
			</tbody>
		</table>
		
		<table>
			<thead>
				<tr>
					<th colspan="2">Waste Generated</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>Metal:</td>
					<td><xsl:value-of select="generated_waste/metal" /></td>
				</tr>
				<tr>
					<td>Deuterium:</td>
					<td><xsl:value-of select="generated_waste/deuterium" /></td>
				</tr>
			</tbody>
		</table>
		
		<p><xsl:value-of select="winner" /></p>
	</xsl:template>
</xsl:stylesheet>